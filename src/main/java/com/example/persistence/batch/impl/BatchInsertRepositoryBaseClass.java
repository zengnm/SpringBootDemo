package com.example.persistence.batch.impl;

import com.example.persistence.batch.BatchInsertRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BatchInsertRepositoryBaseClass<T, ID> extends SimpleJpaRepository<T, ID> implements BatchInsertRepository<T> {
    private EntityManager entityManager;
    private final Class<?> domainClass;
    private final String table;
    private final String[] columns;
    private final Function<T, Object>[] propertyMappers;

    public BatchInsertRepositoryBaseClass(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
        InsertColumnProperties ips = new InsertColumnProperties();
        initInsertColumnProperty(ips);
        this.table = ips.table;
        this.columns = ips.columns.toArray(new String[0]);
        this.propertyMappers = ips.propertyMappers.toArray(new Function[0]);
    }

    protected void initInsertColumnProperty(InsertColumnProperties ips) {
        Table table = this.domainClass.getAnnotation(Table.class);
        if (table == null) {
            // 未指定表，例如多表关联的情况，不支持批量插入
            return ;
        }
        ips.table(table.name());

        int columnCount = 0;
        for (Field field : this.domainClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && column.insertable()) {
                ips.addInsertColumn(column.name(), o -> { //todo 考虑加 `
                    field.setAccessible(true);
                    try {
                        return field.get(o);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                columnCount++;
            }
        }
        if (columnCount == 0) {
            throw new IllegalArgumentException(String.format("class %s insertable column require", this.domainClass.getCanonicalName()));
        }
    }

    protected class InsertColumnProperties {
        private String table;
        private final List<String> columns = new ArrayList<>();
        private final List<Function<T, Object>> propertyMappers = new ArrayList<>();

        public InsertColumnProperties table(String table) {
            this.table = table;
            return this;
        }

        public InsertColumnProperties addInsertColumn(String column, Function<T, Object> propertyMapper) {
            this.columns.add(column);
            this.propertyMappers.add(propertyMapper);
            return this;
        }
    }

    private final static String INSERT_TABLE_COLUMNS_VALUES = "insert into %s(%s)\nvalues%s";

    public int batchInsert(List<T> entities) {
        if (table == null) {
            throw new UnsupportedOperationException("class %s not @Table annotation");
        }

        StringJoiner sj = new StringJoiner(",");
        for (String column : columns) {
            sj.add(column);
        }
        String columns = sj.toString();

        String line = IntStream.range(0, this.columns.length).boxed().map(e -> "?").collect(Collectors.joining(",", "(", ")"));
        String lines = IntStream.range(0, entities.size()).boxed().map(e -> line).collect(Collectors.joining(","));
        String sql = String.format(INSERT_TABLE_COLUMNS_VALUES, this.table, columns, lines);

        int i = 1;
        Query query = entityManager.createNativeQuery(sql);
        for (T testData : entities) {
            for (int j = 0; j < this.columns.length; j++) {
                query.setParameter(i++, this.propertyMappers[j].apply(testData));
            }
        }
        return query.executeUpdate();
    }
}

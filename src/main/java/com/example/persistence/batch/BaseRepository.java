package com.example.persistence.batch;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoRepositoryBean
public abstract class BaseRepository<T, ID> extends SimpleJpaRepository<T, ID> {
    private final Class<T> domainClass;
    private final EntityManager entityManager;
    private final String table;
    private final String[] columns;
    private final Function<T, Object>[] propertyMappers;

    public BaseRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
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
            throw new IllegalArgumentException(String.format("class %s @Table annotation require", this.domainClass.getCanonicalName()));
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

    @Transactional
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
//        throw new RuntimeException("请使用batchInsert"); todo 抛出异常？
        return super.saveAll(entities);
    }
}

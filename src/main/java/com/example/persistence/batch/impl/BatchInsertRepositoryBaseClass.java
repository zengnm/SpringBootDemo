package com.example.persistence.batch.impl;

import com.example.persistence.batch.BatchInsertRepository;
import com.example.persistence.batch.JdbcTemplateHolder;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final Field idField;

    public BatchInsertRepositoryBaseClass(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
        InsertColumnProperties ips = new InsertColumnProperties();
        initInsertColumnProperty(ips);
        this.table = ips.table;
        this.columns = ips.columns.toArray(new String[0]);
        this.propertyMappers = ips.propertyMappers.toArray(new Function[0]);
        this.idField = ips.idField;
    }

    protected void initInsertColumnProperty(InsertColumnProperties ips) {
        Table table = this.domainClass.getAnnotation(Table.class);
        if (table == null) {
            // 未指定表，例如多表关联的情况，不支持批量插入
            return;
        }
        ips.table(table.name());

        int columnCount = 0;
        for (Field field : this.domainClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (field.getAnnotation(Id.class) != null) {
                field.setAccessible(true);
                ips.idField(field);
            } else if (column != null && column.insertable()) {
                field.setAccessible(true);
                ips.addInsertColumn(column.name(), o -> { //todo 考虑加 `
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
        if (ips.idField == null) {
            throw new IllegalArgumentException(String.format("class %s id column not found", this.domainClass.getCanonicalName()));
        }
    }

    protected class InsertColumnProperties {
        private String table;
        private final List<String> columns = new ArrayList<>();
        private final List<Function<T, Object>> propertyMappers = new ArrayList<>();
        private Field idField;

        public InsertColumnProperties table(String table) {
            this.table = table;
            return this;
        }

        public InsertColumnProperties addInsertColumn(String column, Function<T, Object> propertyMapper) {
            this.columns.add(column);
            this.propertyMappers.add(propertyMapper);
            return this;
        }

        public InsertColumnProperties idField(Field idField) {
            this.idField = idField;
            return this;
        }
    }

    private final static String INSERT_TABLE_COLUMNS_VALUES = "insert into %s(%s)\nvalues%s";
    private final static String INSERT_IGNORE_TABLE_COLUMNS_VALUES = "insert ignore into %s(%s)\nvalues%s";
    private final static String INSERT_TABLE_COLUMNS_VALUES_ON_DUPLICATE_UPDATE = "insert ignore into %s(%s)\nvalues%s on duplicate key update %s";

    private String getSql(List<T> entities, int type, String onDuplicateUpdate) {
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
        return switch (type) {
            case 1 ->  String.format(INSERT_TABLE_COLUMNS_VALUES, this.table, columns, lines);
            case 2 ->  String.format(INSERT_IGNORE_TABLE_COLUMNS_VALUES, this.table, columns, lines);
            default ->  String.format(INSERT_TABLE_COLUMNS_VALUES_ON_DUPLICATE_UPDATE, this.table, columns, lines, onDuplicateUpdate);
        };
    }

    @Override
    @Transactional
    public int batchInsert(List<T> entities) {
        String sql = getSql(entities, 1, null);

        int i = 1;
        Query query = entityManager.createNativeQuery(sql);
        for (T testData : entities) {
            for (int j = 0; j < this.columns.length; j++) {
                query.setParameter(i++, this.propertyMappers[j].apply(testData));
            }
        }
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int batchInsertIgnore(List<T> entities) {
        String sql = getSql(entities, 2, null);

        int i = 1;
        Query query = entityManager.createNativeQuery(sql);
        for (T testData : entities) {
            for (int j = 0; j < this.columns.length; j++) {
                query.setParameter(i++, this.propertyMappers[j].apply(testData));
            }
        }
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int batchInsertOnDuplicateUpdate(List<T> entities, String onDuplicateUpdate) {
        String sql = getSql(entities, 3, null);

        int i = 1;
        Query query = entityManager.createNativeQuery(sql);
        for (T testData : entities) {
            for (int j = 0; j < this.columns.length; j++) {
                query.setParameter(i++, this.propertyMappers[j].apply(testData));
            }
        }
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int batchInsertGeneratedKey(List<T> entities) {
        String sql = getSql(entities, 1, null);

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        int[] affects = JdbcTemplateHolder.jdbcTemplate.batchUpdate(con -> con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int ignore) throws SQLException {
                int i = 1;
                for (T entity : entities) {
                    for (int j = 0; j < propertyMappers.length; j++) {
                        ps.setObject(i++, propertyMappers[j].apply(entity));
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return 1;
            }
        }, generatedKeyHolder);
        List<Map<String, Object>> keyList = generatedKeyHolder.getKeyList();
        for (int i = 0; i < keyList.size(); i++) {
            Long id = Long.valueOf(keyList.get(i).get("GENERATED_KEY").toString());
            try {
                idField.set(entities.get(i), id);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        int total = 0;
        for (int affect : affects) {
            total += affect;
        }
        return total;
    }
}

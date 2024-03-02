package com.example.persistence.batch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoRepositoryBean
public abstract class BaseRepository<T, ID> extends SimpleJpaRepository<T, ID> {
    private final EntityManager entityManager;
    private final InsertColumnProperties ips;

    public BaseRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        ips = new InsertColumnProperties();
        initInsertColumnProperty(ips);
        // todo check insertColumnProperties
    }

    protected abstract void initInsertColumnProperty(InsertColumnProperties ips);

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
        // todo 考虑优化性能： list -> array， cache
        int columnCount = ips.columns.size();
        String columns = String.join(", ", ips.columns);
        String line = IntStream.range(0, columnCount).boxed().map(e -> "?")
                .collect(Collectors.joining(",", "(", ")"));
        String lines = IntStream.range(0, entities.size()).boxed().map(e -> line)
                .collect(Collectors.joining(","));
        String sql = String.format(INSERT_TABLE_COLUMNS_VALUES, ips.table, columns, lines);

        int i = 1;
        Query query = entityManager.createNativeQuery(sql);
        for (T testData : entities) {
            for (int j = 0; j < columnCount; j++) {
                query.setParameter(i++, ips.propertyMappers.get(j).apply(testData));
            }
        }
        return query.executeUpdate();
    }

    @Transactional
    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
//        throw new RuntimeException("请使用batchInsert"); todo 打开注释？
        return super.saveAll(entities);
    }
}

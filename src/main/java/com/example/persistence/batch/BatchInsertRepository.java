package com.example.persistence.batch;

import java.util.List;

public interface BatchInsertRepository<T> {
    int batchInsert(List<T> entities);

    int batchInsertIgnore(List<T> entities);

    int batchInsertOnDuplicateUpdate(List<T> entities, String onDuplicateUpdate);

    int batchInsertGeneratedKey(List<T> entities);
}

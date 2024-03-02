package com.example.persistence.batch;

import java.util.List;

public interface BatchInsertRepository<T> {
    int batchInsert(List<T> entities);
}

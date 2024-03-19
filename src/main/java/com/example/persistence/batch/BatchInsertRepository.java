package com.example.persistence.batch;

import java.util.List;

/**
 * 批量插入
 *
 * @author zengnianmei
 */
public interface BatchInsertRepository<T> {
    /**
     * 通过一条sql执行批量插入，相比saveAll明显提升性能，但仅能返回影响函数而不知道插入后数据库实际的数据
     * 需要考虑sql大小，由业务层分页
     *
     * @param entities 待插入数据，自增主键不会回显
     * @return 插入的数量
     */
    int batchInsert(List<T> entities);

    /**
     * 同batchInsert，采用insert ignore into构造sql
     *
     * @param entities 带插入数据，自增主键不会回显
     * @return 插入的数量
     */
    int batchInsertIgnore(List<T> entities);

    /**
     * 同batchInsert，可拼接on duplicate update语句
     *
     * @param entities 带插入数据，自增主键不会回显
     * @return 插入的数量
     */
    int batchInsertOnDuplicateUpdate(List<T> entities, String onDuplicateUpdate);

    /**
     * 同batchInsert，插入成功后，自增主键会写会@Id标记的字段
     *
     * @param entities 带插入数据，自增主键会回显
     * @return 插入的数量
     */
    int batchInsertGeneratedKey(List<T> entities);
}

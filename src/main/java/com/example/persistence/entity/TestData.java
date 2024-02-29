package com.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试数据
 */
@NamedNativeQuery(
        name = "queryDataProperty",
        query = """
                select d.id, d.name, d.status, p.content,  d.create_time, d.update_time
                from test_data d left join test_property p on d.name = p.name
                where d.id > :id
                """,
//        resultClass = TestDataProperty.class,
        resultSetMapping = "testDataPropertyMapping"
)

@SqlResultSetMapping(
        name = "testDataPropertyMapping",
        entities = @EntityResult(entityClass = TestDataProperty.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "name", column = "name"),
                        @FieldResult(name = "status", column = "status"),
                        @FieldResult(name = "content", column = "content"),
                        @FieldResult(name = "createTime", column = "create_time"),
                        @FieldResult(name = "updateTime", column = "update_time"),
                }
        )
)

@Data
@Entity
@Table(name = "test_data")
public class TestData {

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

}

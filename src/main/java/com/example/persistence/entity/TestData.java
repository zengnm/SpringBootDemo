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
                select d.id, d.name, d.status, p.content,  d.create_time as createTime, d.update_time as updateTime
                from test_data d left join test_property p on d.name = p.name
                where d.id = :id
                """,
//        resultClass = TestDataProperty.class,
        resultSetMapping = "testDataPropertyMapping"
)

@SqlResultSetMapping(
        name = "testDataPropertyMapping",
        classes = @ConstructorResult(
                targetClass = TestDataProperty.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "status", type = Integer.class),
                        @ColumnResult(name = "content", type = String.class),
                        @ColumnResult(name = "createTime", type = LocalDateTime.class),
                        @ColumnResult(name = "updateTime", type = LocalDateTime.class),
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

package com.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 测试数据
 */
// 下面两个注解只能标注在entity上, 成对出现
@NamedNativeQuery(name = "queryPart",
        query = """
                select d.id, d.name, d.status, d.create_time
                from test_data d
                where d.id = :id
                """, resultSetMapping = "queryPartByIdMapping")
@SqlResultSetMapping(name = "queryPartByIdMapping"
        , entities = {
            @EntityResult(
                    entityClass = TestData.class,
                    fields = {
                            @FieldResult(name = "id", column = "id"),
                            @FieldResult(name = "name", column = "name"),
                            @FieldResult(name = "status", column = "status"),
                            // name要和字段@Column.name,sql的保持一致
                            @FieldResult(name = "createTime", column = "create_time"),
                    }
            )
        }
//        , classes = {
//                @ConstructorResult(
//                        targetClass = TestData.class,
//                        columns = {
//                                @ColumnResult(name = "id", type = Long.class),
//                                @ColumnResult(name = "name", type = String.class),
//                                @ColumnResult(name = "status", type = Integer.class),
//                                // name要和字段名、sql的保持一致，@Column.name不生效
//                                @ColumnResult(name = "createTime", type = LocalDateTime.class)
//                        }
//                )
//        }
)
@Data
@Entity
@Table(name = "test_data")
@Accessors(chain = true)
public class TestData {

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false, insertable = false)
    @GeneratedValue(strategy = IDENTITY)
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
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;

}

package com.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 测试数据
 */

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

package com.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 属性
 */
@Data
@Entity
@Table(name = "test_property")
public class TestProperty {

    /**
     * 主键
     */
    @Id
    @Column(name = "id", insertable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名成
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 内容
     */
    @Column(name = "content", nullable = false)
    private String content;

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

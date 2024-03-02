package com.example.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TestDataProperty {

    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 名成
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 指定字段不映射，即使数据库中有对应字段
     */
    @Transient
    private Integer ignore;
}

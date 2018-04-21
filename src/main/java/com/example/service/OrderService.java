package com.example.service;

import java.util.List;

import com.example.model.OrderEntity;
import com.github.pagehelper.PageInfo;


/**
 * @author zengnianmei
 * @version 1.0, 2017/10/20.
 */
public interface OrderService {
    List<OrderEntity> find();

    PageInfo<OrderEntity> selectPage(int pageNum, int pageSize, OrderEntity entity);

    int insert(OrderEntity entity);

    int deleteById(long id);

    int updateById(OrderEntity entity);
}

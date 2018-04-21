package com.example.mapper;

import java.util.List;

import com.example.model.OrderEntity;
import com.example.util.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zengnianmei
 * @version 1.0, 2017/10/20.
 */
@Mapper
public interface OrderMapper extends MyMapper<OrderEntity> {
    @Select("select * from demo_order")
    List<OrderEntity> find();
}

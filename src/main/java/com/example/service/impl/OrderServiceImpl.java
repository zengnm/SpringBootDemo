package com.example.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import com.example.mapper.OrderMapper;
import com.example.model.OrderEntity;
import com.example.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zengnianmei
 * @version 1.0, 2017/10/20.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<OrderEntity> find() {
        return orderMapper.selectAll();
    }

    @Override
    public PageInfo<OrderEntity> selectPage(int pageNum, int pageSize, OrderEntity entity){
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(orderMapper.select(entity));
    }

    @Override
    public int insert(OrderEntity entity){
        return orderMapper.insert(entity);
    }

    @Override
    public int deleteById(long id){
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        return orderMapper.deleteByPrimaryKey(entity);
    }

    @Override
    public int updateById(OrderEntity entity){
        return orderMapper.updateByPrimaryKeySelective(entity);
    }

    /**
     * spring boot 定时任务
     */
    @Scheduled(cron = "0 * * * * ?")
    public void reportCurrentTime() {
        LOGGER.info("CurrentTime:" + DATE_TIME_FORMATTER.format(LocalDateTime.now()));
    }
}

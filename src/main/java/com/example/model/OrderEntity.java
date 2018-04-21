package com.example.model;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zengnianmei
 * @version 1.0, 2017/10/20.
 */
@Table(name = "demo_order")
public class OrderEntity extends BaseEntity {
    // 用户pin
    @Column(name = "user_pin")
    private String userPin;
    // 订单金额
    @Column(name = "order_price")
    private String orderPrice;

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                ", id=" + id +
                ", created=" + created +
                ", modified=" + modified +
                "userPin='" + userPin + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                '}';
    }
}

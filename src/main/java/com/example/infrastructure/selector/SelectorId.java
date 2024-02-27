package com.example.infrastructure.selector;

/**
 * 继承本接口用于扫描器识别策略，同时指定每个策略的id
 *
 * @author zengnianmei
 */
public interface SelectorId {
    /**
     * 策略id，每种策略的id不能重复
     */
    String id();
}

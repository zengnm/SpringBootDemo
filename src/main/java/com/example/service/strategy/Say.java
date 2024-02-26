package com.example.service.strategy;

import com.example.infrastructure.selector.Strategy;
import com.example.infrastructure.selector.StrategyId;

/**
 * @author zengnianmei
 */
@Strategy
public interface Say extends StrategyId {
    void say();
}

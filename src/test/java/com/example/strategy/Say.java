package com.example.strategy;

import com.example.infrastructure.selector.Selector;
import com.example.infrastructure.selector.SelectorId;

/**
 * @author zengnianmei
 */
public interface Say extends SelectorId {
    @Selector(determine = "#param.type")
    String say(Param param);
}

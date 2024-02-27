package com.example.infrastructure.selector;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定从哪个参数及字段获取策略id
 *
 * @author zengnianmei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Selector {
    /**
     * 通过SpEL指定策略id
     */
    String determine();
}

package com.example.infrastructure.selector;

import com.google.common.reflect.AbstractInvocationHandler;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.CheckForNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SelectableComponentProxyInvocation extends AbstractInvocationHandler {
    private volatile Map<String, ?> strategyMap;
    private final ObjectProvider<?> objectProvider;

    private SelectableComponentProxyInvocation(ObjectProvider<?> objectProvider) {
        this.objectProvider = objectProvider;
    }

    public static Supplier<Object> createProxy(ObjectProvider<?> objectProvider, ClassLoader classLoader,
                                               Class<?>[] classes) {
        SelectableComponentProxyInvocation invocation = new SelectableComponentProxyInvocation(objectProvider);
        return () -> Proxy.newProxyInstance(classLoader, classes, invocation);
    }

    @CheckForNull
    @Override
    protected Object handleInvocation(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
        // 调用代理类的id()方法，直接返回null
        if ("id".equals(method.getName()) && method.getParameterCount() == 0) {
            return null;
        }

        ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Selector annotation = method.getAnnotation(Selector.class);
        String determine = annotation.determine();
        SpelExpressionParser parser = new SpelExpressionParser();
        String key = parser.parseExpression(determine).getValue(context, String.class);
        Object object = getStrategyMap().get(key);
        try {
            return method.invoke(object, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private Map<String, ?> getStrategyMap() {
        if (this.strategyMap == null) {
            synchronized (this) {
                if (this.strategyMap == null) {
                    strategyMap = objectProvider.stream()
                            .map(e -> (SelectorId) e)
                            .filter(e -> e.id() != null) // 排除当前代理类
                            .collect(Collectors.toMap(SelectorId::id, Function.identity()));
                }
            }
        }
        return strategyMap;
    }
}

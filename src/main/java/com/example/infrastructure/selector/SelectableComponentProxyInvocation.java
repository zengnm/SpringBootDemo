package com.example.infrastructure.selector;

import com.google.common.reflect.AbstractInvocationHandler;
import org.checkerframework.checker.nullness.qual.Nullable;
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
import java.util.function.Supplier;

public class SelectableComponentProxyInvocation extends AbstractInvocationHandler {
    private final Map<String, ?> strategyMap;

    private SelectableComponentProxyInvocation(Map<String, ?> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public static Supplier<Object> createProxy(Map<String, ?> strategyMap, ClassLoader classLoader,
                                               Class<?>[] classes) {
        SelectableComponentProxyInvocation invocation = new SelectableComponentProxyInvocation(strategyMap);
        return () -> {
            Object o = Proxy.newProxyInstance(classLoader, classes, invocation);
            System.out.println(o);
            return o;
        };
    }

    @CheckForNull
    @Override
    protected Object handleInvocation(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
        ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);

        Selector annotation = method.getAnnotation(Selector.class);
        String determine = annotation.determine();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        SpelExpressionParser parser = new SpelExpressionParser();
        String key = parser.parseExpression(determine).getValue(context, String.class);
        Object object = strategyMap.get(key);
        try {
            return method.invoke(object, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}

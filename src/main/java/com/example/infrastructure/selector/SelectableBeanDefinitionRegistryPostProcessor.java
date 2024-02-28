package com.example.infrastructure.selector;

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SelectableBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(@Nonnull BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        List<String> basePackages = AutoConfigurationPackages.get(beanFactory);
        List<BeanDefinition> candidates = getCandidates(basePackages);

        for (BeanDefinition beanDefinition : candidates) {
            Class<?> clazz = getClassByName(beanDefinition.getBeanClassName());
            Map<String, ?> beansOfType = beanFactory.getBeansOfType(clazz).values().stream()
                    .collect(Collectors.toMap(e -> ((SelectorId) e).id(), e -> e));

            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(clazz);
            definition.setPrimary(true);
            definition.setAutowireCandidate(true);
            definition.setInstanceSupplier(SelectableComponentProxyInvocation.createProxy(beansOfType,
                    clazz.getClassLoader(), new Class[]{clazz}));
            String beanName = StringUtils.uncapitalize(clazz.getSimpleName());
            registry.registerBeanDefinition(beanName, definition);
        }
    }

    private Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BeanDefinition> getCandidates(List<String> basePackages) {
        SelectableComponentProvider provider = new SelectableComponentProvider();
        return basePackages.stream()
                .flatMap(pkg -> provider.findCandidateComponents(pkg).stream())
                .toList();
    }
}


package com.example.infrastructure.selector;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.util.Streamable;

import java.util.List;

/**
 * ImportBeanDefinitionRegistrar
 *
 * @author zengnianmei
 */
public class StrategyBeanRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private List<String> getBasePackages() {
        return AutoConfigurationPackages.get(this.beanFactory);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        System.out.println(getBasePackages());
        Streamable<BeanDefinition> candidates = getCandidates();

        for (BeanDefinition beanDefinition : candidates.stream().toList()) {
            System.out.println("strategy beanClassName: " + beanDefinition.getBeanClassName());
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(importingClassMetadata, registry, null);
    }

    public Streamable<BeanDefinition> getCandidates() {
        StrategyComponentProvider provider = new StrategyComponentProvider();
        return Streamable.of(() -> getBasePackages().stream()
                .mapMulti((pkg, buf) -> provider.findCandidateComponents(pkg).forEach(buf)));

    }
}

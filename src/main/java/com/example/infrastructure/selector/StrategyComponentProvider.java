package com.example.infrastructure.selector;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;

/**
 * @author zengnianmei
 */
public class StrategyComponentProvider extends ClassPathScanningCandidateComponentProvider {

    public StrategyComponentProvider() {
        super(false);
        addIncludeFilter(new InterfaceTypeFilter(StrategyId.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isNonStrategyIdInterface = !StrategyId.class.getName().equals(beanDefinition.getBeanClassName());
        boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();

        return isNonStrategyIdInterface && isTopLevelType;
    }

    private static class InterfaceTypeFilter extends AssignableTypeFilter {

        public InterfaceTypeFilter(Class<?> targetType) {
            super(targetType);
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                throws IOException {

            return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
        }
    }
}

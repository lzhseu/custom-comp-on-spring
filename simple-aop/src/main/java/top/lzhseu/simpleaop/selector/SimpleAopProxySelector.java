package top.lzhseu.simpleaop.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import top.lzhseu.simpleaop.spring.SimpleAopBeanFactoryPostProcessor;
import top.lzhseu.simpleaop.spring.SimpleAopBeanPostProcessor;

/**
 * @author lzh
 * @date 2021/1/3 10:14
 */
public class SimpleAopProxySelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{SimpleAopBeanFactoryPostProcessor.class.getName(),
                SimpleAopBeanPostProcessor.class.getName()};
    }
}

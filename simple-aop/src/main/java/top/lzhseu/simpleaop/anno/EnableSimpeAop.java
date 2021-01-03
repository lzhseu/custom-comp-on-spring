package top.lzhseu.simpleaop.anno;

import org.springframework.context.annotation.Import;
import top.lzhseu.simpleaop.selector.SimpleAopProxySelector;

import java.lang.annotation.*;

/**
 * @author lzh
 * @date 2021/1/3 10:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SimpleAopProxySelector.class)
public @interface EnableSimpeAop {
}

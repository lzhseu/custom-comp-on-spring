package top.lzhseu.simpleaop.anno;

import top.lzhseu.simpleaop.enums.PointCutTypeEum;

import java.lang.annotation.*;

/**
 * @author lzh
 * @date 2020/12/31 17:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointCut {
    String[] pointcuts();
    PointCutTypeEum type();
}

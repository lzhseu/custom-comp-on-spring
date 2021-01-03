package top.lzhseu.simpleaop.holder;

import lombok.*;
import top.lzhseu.simpleaop.enums.PointCutTypeEum;

import java.lang.reflect.Method;

/**
 * @author lzh
 * @date 2021/1/2 15:56
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ProxyBeanHolder {

    /**
     * 被增强的方法
     */
    private Method waitForAdviceMethod;


    /**
     * 切面类的全限定名
     */
    private String aspectClassName;

    /**
     * 增强方法
     */
    private Method adviceMethod;

    /**
     * 增强方法的类型
     */
    private PointCutTypeEum adviceType;


}

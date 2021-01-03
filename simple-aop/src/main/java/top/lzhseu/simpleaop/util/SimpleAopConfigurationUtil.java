package top.lzhseu.simpleaop.util;

import top.lzhseu.simpleaop.anno.PointCut;
import top.lzhseu.simpleaop.holder.ProxyBeanHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzh
 * @date 2021/1/2 16:24
 */
public class SimpleAopConfigurationUtil {

    public static final String ASPECT_ANNOTATION_CLASSNAME = "top.lzhseu.simpleaop.anno.Aspect";

    public static final Class<PointCut> POINTCUT_CLASS = PointCut.class;


    public static final Map<String, List<ProxyBeanHolder>> PROXY_BEAN_HOLDERS = new ConcurrentHashMap<>();

}

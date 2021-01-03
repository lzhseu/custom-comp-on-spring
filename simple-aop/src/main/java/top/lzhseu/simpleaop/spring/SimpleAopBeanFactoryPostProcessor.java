package top.lzhseu.simpleaop.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import top.lzhseu.simpleaop.anno.PointCut;
import top.lzhseu.simpleaop.enums.PointCutTypeEum;
import top.lzhseu.simpleaop.holder.ProxyBeanHolder;
import top.lzhseu.simpleaop.util.SimpleAopConfigurationUtil;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lzh
 * @date 2021/1/2 15:20
 */
@Slf4j
public class SimpleAopBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        // 获取所有的 BeanDefinition 的名称
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();

        // 循环获取每个 BeanDefinition 名称
        for (String beanDefinitionName : beanDefinitionNames) {

            // 根据 BeanDefinition 的名称得到 BeanDefinition
            BeanDefinition bd = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);

            // 判断该 BeanDefinition 是否是一个注解的 BeanDefinition
            if (bd instanceof AnnotatedBeanDefinition) {

                // 获取其上所有注解
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) bd).getMetadata();
                Set<String> annotationTypes = metadata.getAnnotationTypes();

                // 循环所有注解，找到 @Aspect 注解类
                for (String annotation : annotationTypes) {
                    if (annotation.equals(SimpleAopConfigurationUtil.ASPECT_ANNOTATION_CLASSNAME)) {

                        log.info("find an aspect: [{}]", bd.getBeanClassName());

                        // 扫描该类，解析类中的方法，并进行注册
                        doScan((GenericBeanDefinition) bd);
                    }
                }
            }

        }
    }

    /**
     * 扫描并解析注解了 @Aspect 的类
     * @param gbd BeanDefinition
     */
    private void doScan(GenericBeanDefinition gbd) {


        try {
            // 获取类对象
            Class clazz = Class.forName(gbd.getBeanClassName());

            // 获取所有的方法
            Method[] methods = clazz.getMethods();

            // 遍历所有方法，找出被 @PointCut 注解的方法
            for (Method method : methods) {

                // 如果注解了 @PointCut
                if (method.isAnnotationPresent(SimpleAopConfigurationUtil.POINTCUT_CLASS)) {

                    log.info("find a advice method: [{}]", method.getName());

                    // 解析这个注解
                    doScan(method, method.getAnnotation(SimpleAopConfigurationUtil.POINTCUT_CLASS));

                }
            }

        } catch (ClassNotFoundException e) {
            log.error("获取类 [{}] 对象失败", gbd.getBeanClassName(), e);
        }

    }

    /**
     * 解析 @PointCut 注解，并注册需要增强的类
     * @param method 增强的方法
     * @param annotation 注解
     */
    private void doScan(Method method, Annotation annotation) {

        final PointCut pointCut = (PointCut) annotation;
        final Map<String, List<ProxyBeanHolder>> proxyBeanHolders = SimpleAopConfigurationUtil.PROXY_BEAN_HOLDERS;


        if (pointCut != null) {

            // 拿出属性
            String[] pointcuts = pointCut.pointcuts();
            PointCutTypeEum type = pointCut.type();

            // 拿出待增强的每个方法名
            for (String pointCutExpr : pointcuts) {

                // 解析出待增强的类的全限定名
                String className = parseForClassName(pointCutExpr);

                // 如果类名为空，则跳过
                if (className.isEmpty()) {
                    continue;
                }

                // 解析出方法
                Method waitForAdviceMethod;
                try {
                    waitForAdviceMethod = parseForMethod(className, pointCutExpr);
                    if (waitForAdviceMethod == null) {
                        continue;
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Class not found: [{}]", className);
                    continue;
                }

                // 如果注册表中没有该类名，则新建一个 list
                if (!proxyBeanHolders.containsKey(className)) {
                    proxyBeanHolders.put(className, new CopyOnWriteArrayList<>());
                }

                ProxyBeanHolder proxyBeanHolder = ProxyBeanHolder.builder()
                        .waitForAdviceMethod(waitForAdviceMethod)
                        .aspectClassName(method.getDeclaringClass().getName())
                        .adviceMethod(method)
                        .adviceType(type).build();

                proxyBeanHolders.get(className).add(proxyBeanHolder);

                log.info("parse a ProxyBeanHolder: [{}]", proxyBeanHolder);
            }

        }

    }


    /**
     * 解析类名
     * @param pointCutExpr 切点表达式
     * @return 类的全限定名
     */
    private String parseForClassName(String pointCutExpr) {

        pointCutExpr = pointCutExpr.trim();


        int idx = pointCutExpr.lastIndexOf("(");

        if (idx == -1) {
            return pointCutExpr.substring(0, pointCutExpr.lastIndexOf("."));
        }

        String className = pointCutExpr.substring(0, idx);

        int idx2 = className.lastIndexOf('.');

        if (idx2 == -1) {
            return className;
        }

        return className.substring(0, idx2);
    }

    /**
     * 解析方法
     * @param className 类全限定名
     * @param pointCutExpr 切点表达式
     */
    private Method parseForMethod(String className, String pointCutExpr) throws ClassNotFoundException {

        String methodExpr = pointCutExpr.substring(className.length() + 1);

        Class<?> clazz = Class.forName(className);

        methodExpr = methodExpr.trim();

        try {
            int firstIndex = methodExpr.indexOf("(");
            int secondIndex = methodExpr.indexOf(")");

            if (firstIndex == -1) {
                return clazz.getMethod(methodExpr);
            }

            String methodName = methodExpr.substring(0, firstIndex);

            if (secondIndex == -1) {
                return clazz.getMethod(methodName);
            }

            String argsTypeStr = methodExpr.substring(firstIndex + 1, secondIndex);

            String[] argsTypesArr = argsTypeStr.split(",");

            Class<?>[] argsTypeClass = new Class[argsTypesArr.length];

            try {
                int i = 0;
                for (String argType : argsTypesArr) {
                    argsTypeClass[i++] = getArgClass(argType);
                }
            } catch (ClassNotFoundException e) {
                log.error("Error occurred when parsing parameters", e);
            }


            return clazz.getMethod(methodName, argsTypeClass);
            
        } catch (NoSuchMethodException e) {
            log.error("Class [{}] has no method [{}]", clazz, methodExpr, e);
        }

        return null;

    }


    private Class getArgClass(String str) throws ClassNotFoundException {
        str = str.trim();
        if ("int".equals(str)) {
            return int.class;
        } else if ("long".equals(str)) {
            return long.class;
        } else if ("short".equals(str)) {
            return short.class;
        } else if ("float".equals(str)) {
            return float.class;
        } else if ("double".equals(str)) {
            return double.class;
        } else if ("byte".equals(str)) {
            return byte.class;
        } else if ("char".equals(str)) {
            return char.class;
        } else if ("boolean".equals(str)) {
            return boolean.class;
        } else {
            return Class.forName(str);
        }
    }
}

package top.lzhseu.simpleaop.spring;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import top.lzhseu.simpleaop.holder.ProxyBeanHolder;
import top.lzhseu.simpleaop.interceptor.AspectProxyInterceptor;
import top.lzhseu.simpleaop.util.SimpleAopConfigurationUtil;

import java.util.List;
import java.util.Map;

/**
 * @author lzh
 * @date 2021/1/2 14:57
 */
public class SimpleAopBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Object result = bean;
        final Map<String, List<ProxyBeanHolder>> proxyBeanHolders = SimpleAopConfigurationUtil.PROXY_BEAN_HOLDERS;

        String className = bean.getClass().getName();
        if (proxyBeanHolders.containsKey(className)) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(result.getClass());
            enhancer.setCallback(new AspectProxyInterceptor(proxyBeanHolders.get(className)));
            result = enhancer.create();
        }

        return result;
    }
}

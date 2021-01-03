package top.lzhseu.simpleaop.interceptor;

import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import top.lzhseu.simpleaop.enums.PointCutTypeEum;
import top.lzhseu.simpleaop.holder.ProxyBeanHolder;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lzh
 * @date 2021/1/2 20:07
 */
@Component
@Scope("prototype")
@NoArgsConstructor
@Setter
public class AspectProxyInterceptor implements MethodInterceptor, BeanFactoryAware {

    private BeanFactory beanFactory;

    private List<ProxyBeanHolder> proxyBeanHolders;

    public AspectProxyInterceptor(List<ProxyBeanHolder> proxyBeanHolders) {
        this.proxyBeanHolders = proxyBeanHolders;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        // 调用目标方法之前，处理前置通知和环绕通知
        for (ProxyBeanHolder proxyBeanHolder : proxyBeanHolders) {
            if (proxyBeanHolder.getWaitForAdviceMethod().equals(method) &&
                    (proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.BEFORE) ||
                            proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.AROUND))) {

                Class<?> clazz = Class.forName(proxyBeanHolder.getAspectClassName());
                Object instance = beanFactory.getBean(clazz);
                Method proxyMethod = proxyBeanHolder.getAdviceMethod();
                proxyMethod.invoke(instance);

            }
        }

        // 调用目标方法
        Object result = methodProxy.invokeSuper(o, args);


        // 调用目标方法之后，处理后置通知和环绕通知
        for (ProxyBeanHolder proxyBeanHolder : proxyBeanHolders) {
            if (proxyBeanHolder.getWaitForAdviceMethod().equals(method) &&
                    (proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.AFTER) ||
                            proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.AROUND))) {

                Class<?> clazz = Class.forName(proxyBeanHolder.getAspectClassName());
                Object instance = beanFactory.getBean(clazz);
                Method proxyMethod = proxyBeanHolder.getAdviceMethod();
                proxyMethod.invoke(instance);

            }
        }

        return result;
    }
}

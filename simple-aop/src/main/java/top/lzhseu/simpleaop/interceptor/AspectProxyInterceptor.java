package top.lzhseu.simpleaop.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import top.lzhseu.simpleaop.enums.PointCutTypeEum;
import top.lzhseu.simpleaop.example.AspectLogic;
import top.lzhseu.simpleaop.holder.ProxyBeanHolder;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lzh
 * @date 2021/1/2 20:07
 */
public class AspectProxyInterceptor implements MethodInterceptor {

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

                Class clazz = Class.forName(proxyBeanHolder.getAspectClassName());
                Method proxyMethod = proxyBeanHolder.getAdviceMethod();
                proxyMethod.invoke(clazz.newInstance());

            }
        }

        // 调用目标方法
        Object result = methodProxy.invokeSuper(o, args);


        // 调用目标方法之后，处理后置通知和环绕通知
        for (ProxyBeanHolder proxyBeanHolder : proxyBeanHolders) {
            if (proxyBeanHolder.getWaitForAdviceMethod().equals(method) &&
                    (proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.AFTER) ||
                            proxyBeanHolder.getAdviceType().equals(PointCutTypeEum.AROUND))) {

                Class clazz = Class.forName(proxyBeanHolder.getAspectClassName());
                Method proxyMethod = proxyBeanHolder.getAdviceMethod();
                proxyMethod.invoke(clazz.newInstance());

            }
        }

        return result;
    }
}

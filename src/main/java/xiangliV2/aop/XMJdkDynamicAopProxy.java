package xiangliV2.aop;

import xiangliV2.aop.support.XMAdvicedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class XMJdkDynamicAopProxy implements XMAopProxy, InvocationHandler {

    private XMAdvicedSupport advised;

    public XMJdkDynamicAopProxy(XMAdvicedSupport config) {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(advised.getClass().getClassLoader(), advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, advised.getTargetClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptionAdvice = advised.getInterceptorsAndDynamicInterceptionAdvice(method, advised.getTargetClass());
        XMMethodInvocation mi = new XMMethodInvocation(proxy, advised.getTargetInstance(), method, args, advised.getTargetClass(), interceptionAdvice);
        return mi.proceed();
    }
}

package xiangliV2.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class XMCglibAopProxy implements XMAopProxy, InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}

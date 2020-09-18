package xiangliV2.aop.aspect;

import xiangliV2.aop.XMMethodInterceptor;
import xiangliV2.aop.XMMethodInvocation;

import java.lang.reflect.Method;

public class XMAspectJBeforeAdvice extends XMAbstractAspectJAdvice implements XMMethodInterceptor {

    public XMAspectJBeforeAdvice(
            Method aspectJBeforeAdviceMethod, Object instance) {

        super(aspectJBeforeAdviceMethod, instance);
    }

    @Override
    public Object invoke(XMMethodInvocation invocation) {

        try {
            invokeAdviceMethod(invocation, null, null);
            return invocation.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

        }
        
        return null;
    }
}



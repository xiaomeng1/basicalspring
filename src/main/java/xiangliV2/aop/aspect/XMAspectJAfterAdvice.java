package xiangliV2.aop.aspect;

import xiangliV2.aop.XMJoinPoint;
import xiangliV2.aop.XMMethodInterceptor;
import xiangliV2.aop.XMMethodInvocation;

import java.lang.reflect.Method;

public class XMAspectJAfterAdvice extends XMAbstractAspectJAdvice implements XMMethodInterceptor {

    private XMJoinPoint joinPoint;

    public XMAspectJAfterAdvice(
            Method aspectJBeforeAdviceMethod, Object instance) {

        super(aspectJBeforeAdviceMethod, instance);
    }


    @Override
    public Object invoke(XMMethodInvocation invocation) {
        try {
            return invocation.proceed();
        } catch (Throwable ex) {
            throw new RuntimeException("fail");
        } finally {
            try {
                invokeAdviceMethod(invocation, null, null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

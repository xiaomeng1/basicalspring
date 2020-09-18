package xiangliV2.aop.aspect;

import lombok.Data;
import xiangliV2.aop.XMJoinPoint;

import java.lang.reflect.Method;

@Data
public abstract class XMAbstractAspectJAdvice implements XMAdvice {

    private Object aspectInstance;

    private Method aspectMethod;

    private Object[] aspectMethodArgs;

    public XMAbstractAspectJAdvice(
            Method aspectJAdviceMethod, Object aspectInstance) {

        this.aspectMethod = aspectJAdviceMethod;
        this.aspectInstance = aspectInstance;
    }

    protected Object invokeAdviceMethod(XMJoinPoint jp, Object returnValue, Throwable t) throws Throwable {

        return aspectMethod.invoke(aspectInstance, null);
    }

}

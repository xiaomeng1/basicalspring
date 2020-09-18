package xiangliV2.aop;

import java.lang.reflect.Method;

public interface XMJoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    void setUserAttribute(String key,Object value);

    Object getUserAttribute(String key);
}

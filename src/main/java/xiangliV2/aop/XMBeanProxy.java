package xiangliV2.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xiangliV2.aop.aspect.XMAdvice;
import xiangliV2.aop.support.XMAdvicedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XMBeanProxy implements InvocationHandler {

    private XMAdvicedSupport advicedSupport;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        List<XMAdvice> advice = advicedSupport.getAdvice(method);
//        processAdvice(advice.get(0));
//        Object invoke = method.invoke(advicedSupport.getTargetInstance(), args);
//        processAdvice(advice.get(1));
//
//
//        return invoke;
        return null;
    }


//    public Object newProxy() {
//        return Proxy.newProxyInstance(advicedSupport.getTargetClass().getClassLoader(), advicedSupport.getTargetClass().getInterfaces(), this);
//    }
//
//    private void processAdvice(XMAdvice advice) {
//        Object aspectInstance = advice.getAspectInstance();
//        try {
//            advice.getAspectMethod().invoke(aspectInstance);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}

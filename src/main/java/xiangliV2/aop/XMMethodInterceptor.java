package xiangliV2.aop;

import xiangliV2.aop.aspect.XMAdvice;

public interface XMMethodInterceptor extends XMAdvice {

    Object invoke(XMMethodInvocation invocation);

}

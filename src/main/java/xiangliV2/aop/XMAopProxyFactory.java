package xiangliV2.aop;

import xiangliV2.aop.support.XMAdvicedSupport;

public interface XMAopProxyFactory {

    XMAopProxy createAopProxy(XMAdvicedSupport config);
}

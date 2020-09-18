package xiangliV2.aop;

import xiangliV2.aop.support.XMAdvicedSupport;

public class XMDefaultAopProxyFactory implements XMAopProxyFactory {
    @Override
    public XMAopProxy createAopProxy(XMAdvicedSupport config) {
        return new XMJdkDynamicAopProxy(config);
    }
}

package xiangliV2.context;

import xiangliV2.annotation.XMAutowired;
import xiangliV2.aop.XMAopProxyFactory;
import xiangliV2.aop.XMBeanProxy;
import xiangliV2.aop.XMDefaultAopProxyFactory;
import xiangliV2.aop.config.XMAopConfig;
import xiangliV2.aop.support.XMAdvicedSupport;
import xiangliV2.config.beans.BeanDefinition;
import xiangliV2.config.beans.BeanDefinitionWrapper;
import xiangliV2.core.XMBeanFactory;
import xiangliV2.support.BeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class XMApplicationContext implements XMBeanFactory {

    private static final Map<String, BeanDefinition> beanMap = new HashMap<>();
    private static BeanDefinitionReader beanDefinitionReader = null;
    private static final Map<String, Object> beanInstanceCache = new HashMap<>();
    private static final Map<String, BeanDefinitionWrapper> beanDefinitionWrapperCache = new HashMap<>();

//    private static final List<BeanDefinitionWrapper>

    public XMApplicationContext(String... config) {
        beanDefinitionReader = new BeanDefinitionReader(config);
        List<BeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();
        doRegister(beanDefinitions);
        doAutoWired();
        System.out.println("注入完成....");
    }

    private void doRegister(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.stream().forEach(bean -> {
                    beanMap.put(bean.getFactoryBeanName(), bean);
                    beanMap.put(bean.getClassName(), bean);
                }
        );
    }


    private void doAutoWired() {
        for (Map.Entry<String, BeanDefinition> entry : beanMap.entrySet()) {
            getBean(entry.getValue().getFactoryBeanName());
        }
    }


    public Object getBean(String beanName) {
        try {
            BeanDefinition beanDefinition = beanMap.get(beanName);
            //进行实例化
            Object instance = doInstance(beanDefinition);
            //封装成BeanDefinitionWrap 如果是个接口 则 实现类实例 实现类类名, 接口名称
            BeanDefinitionWrapper instanceWrapper = new BeanDefinitionWrapper(instance, beanDefinition.getClassName(), beanDefinition.getFactoryBeanName());

            beanDefinitionWrapperCache.put(beanName, instanceWrapper);
            //进行缓存并注入

            populate(instanceWrapper);
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException("get bean fail. error msg is " + ex.getStackTrace().toString() + " " + ex.getMessage());
        }

    }


    private void populate(BeanDefinitionWrapper instanceWrapper) throws IllegalAccessException {

        Object classInstance = instanceWrapper.getClassInstance();
        Field[] fields = classInstance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(XMAutowired.class)) {
                continue;
            }
            Class<?> clazz = field.getType();
            field.setAccessible(true);
            Object targetObject = beanInstanceCache.get(beanMap.get(clazz.getName()).getClassName());
            if (targetObject == null) {
                field.set(classInstance, getBean(beanMap.get(clazz.getName()).getFactoryBeanName()));
                continue;
            }

            field.set(classInstance, beanInstanceCache.get(beanMap.get(clazz.getName()).getClassName()));
        }
    }

    private Object doInstance(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        if (beanInstanceCache.containsKey(beanDefinition.getClassName())) {
            return beanInstanceCache.get(beanDefinition.getClassName());
        }

        //需要加判断
        Object object = beanDefinition.getBeanClass().newInstance();
        //aop
        XMAdvicedSupport xmAdvicedSupport = instanceAopConfig(object);
        if (xmAdvicedSupport.matcher()) {
            object = new XMDefaultAopProxyFactory().createAopProxy(xmAdvicedSupport).getProxy();
        }

        beanInstanceCache.put(beanDefinition.getClassName(), object);
        return object;
    }

    private XMAdvicedSupport instanceAopConfig(Object object) {
        XMAopConfig config = new XMAopConfig();
        config.setPointCut(this.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.getConfig().getProperty("aspectAfter"));
        config.setAspectThorw(this.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectThrowName(this.getConfig().getProperty("aspectAfterThrowingName"));
        return new XMAdvicedSupport(config, object.getClass(), object);
    }


    public Object getBean(Class clazz) {
        return getBean(beanMap.get(clazz.getName()).getFactoryBeanName());
    }


    public int getBeanDefinitionCount() {
        return beanMap.size();
    }


    public String[] getBeanDefinitionNames() {
        return beanMap.keySet().toArray(new String[getBeanDefinitionCount()]);
    }

    public Properties getConfig() {
        return beanDefinitionReader.getConfig();
    }
}

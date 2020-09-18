package xiangliV2.aop.support;

import lombok.Data;
import xiangliV2.aop.aspect.XMAspectJAfterAdvice;
import xiangliV2.aop.aspect.XMAspectJBeforeAdvice;
import xiangliV2.aop.config.XMAopConfig;

import java.lang.reflect.Method;
import java.util.*;

@Data
public class XMAdvicedSupport {

    private Map<Method, List<Object>> adviceCache;

    private XMAopConfig aopConfig;

    private Class targetClass;

    private Object targetInstance;

    public XMAdvicedSupport(XMAopConfig aopConfig, Class targetClass, Object targetInstance) {
        this.aopConfig = aopConfig;
        this.targetClass = targetClass;
        this.targetInstance = targetInstance;
        if (this.matcher())
            initAdviceCache();
    }

    private void initAdviceCache() {
        adviceCache = new HashMap<>();
        Method[] methods = targetClass.getMethods();

        List<Method> methodList = Arrays.asList(Object.class.getMethods());
        for (Method method : methods) {
            if (methodList.contains(method)) {
                continue;
            }
            adviceCache.put(method, assembleAdvice());
        }

        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class clazz : interfaces) {
                for (Method method : clazz.getMethods()) {
                    if (methodList.contains(method)) {
                        continue;
                    }
                    adviceCache.put(method, assembleAdvice());
                }
            }
        }
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        // 从缓存中获取
        List<Object> cached = this.adviceCache.get(method);
        // 缓存未命中，则进行下一步处理
        if (cached == null) {
            // 获取所有的拦截器
            cached = this.assembleAdvice();
            // 存入缓存
            this.adviceCache.put(method, cached);
        }
        return cached;
    }


    private List<Object> assembleAdvice() {
        List<Object> adviceList = new ArrayList<>();
        try {
            String aspectClass = aopConfig.getAspectClass();

            Class<?> aspectClazz = Class.forName(aspectClass);
            Object instance = aspectClazz.newInstance();
            aspectClazz.getMethod(aopConfig.getAspectBefore());

            adviceList.add(new XMAspectJAfterAdvice(aspectClazz.getMethod(aopConfig.getAspectAfter()), instance));
            adviceList.add(new XMAspectJBeforeAdvice(aspectClazz.getMethod(aopConfig.getAspectAfter()), instance));

        } catch (Exception ex) {

        }
        return adviceList;
    }

    public boolean matcher() {
        String target = targetClass.getName();
        String pointCut = aopConfig.getPointCut();
        return target.contains(pointCut);
    }
}

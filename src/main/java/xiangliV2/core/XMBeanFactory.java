package xiangliV2.core;

public interface XMBeanFactory {

    Object getBean(String name);


    Object getBean(Class clazz);
}

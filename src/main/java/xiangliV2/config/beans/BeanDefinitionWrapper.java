package xiangliV2.config.beans;

public class BeanDefinitionWrapper {


    private Object classInstance;

    private String className;

    private String beanName;

    public BeanDefinitionWrapper(Object classInstance, String className, String beanName) {
        this.classInstance = classInstance;
        this.className = className;
        this.beanName = beanName;
    }

    public Object getClassInstance() {
        return classInstance;
    }

    public void setClassInstance(Object classInstance) {
        this.classInstance = classInstance;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

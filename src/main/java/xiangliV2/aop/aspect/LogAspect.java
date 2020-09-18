package xiangliV2.aop.aspect;

public class LogAspect {

    public void before() {
        System.out.println("方法前置增强-----------------------");
    }

    public void after() {
        System.out.println("方法后置增强------------------------");
    }


    public void afterThrowing() {
        System.out.println("方法抛出异常时进行增强------------------");
    }
}

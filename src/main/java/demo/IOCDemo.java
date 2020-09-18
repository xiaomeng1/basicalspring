package demo;

import demo.cycledepedency.APerson;
import demo.cycledepedency.BPerson;
import demo.cycledepedency.CPerson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOCDemo {
    static Map<Class, Class> classMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        List<Class> list = new ArrayList<>();
        list.add(APerson.class);
        list.add(BPerson.class);
        list.add(CPerson.class);

        doInstance(APerson.class);
    }

    public static Object doInstance(Class clazz) throws IllegalAccessException, InstantiationException {
        System.out.printf("我要开始实例化了." + clazz.getName());
        Object instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return instance;
        }


        for (Field field : fields) {
            System.out.println("我要注入:" + field.getName() + " 首先实例化 " + field.getType().getName());
            field.set(instance, doInstance(field.getType()));
        }

        return instance;
    }

}

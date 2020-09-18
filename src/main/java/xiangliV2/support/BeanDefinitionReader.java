package xiangliV2.support;

import xiangliV2.annotation.XMController;
import xiangliV2.annotation.XMService;
import xiangliV2.config.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    private static final List<String> beanClassList = new ArrayList<>();
    private String[] configs;
    private Properties applicationProperties;
    private static final List<BeanDefinition> beanDefinitionContext = new ArrayList<>();

    public BeanDefinitionReader(String... config) {
        this.configs = config;

        //加载配置文件
        loadConfig(config);

        //scanner package
        scannerPackage();
    }


    public List<BeanDefinition> loadBeanDefinitions() {
        if (beanClassList.isEmpty()) {
            return new ArrayList<>();
        }

        for (String className : beanClassList) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String[] arr = className.split("\\.");
            String beanName = toLowerFirstCase(arr[arr.length - 1]);
            if (!(clazz.isAnnotationPresent(XMController.class) || clazz.isAnnotationPresent(XMService.class)))
                continue;

            if (clazz.isAnnotationPresent(XMController.class)) {
                beanDefinitionContext.add(assembleBeanDefinition(beanName, className, clazz));
                continue;
            }

            XMService annotation = clazz.getAnnotation(XMService.class);
            String alias = annotation.value();
            if (alias != "") {
                beanDefinitionContext.add(assembleBeanDefinition(beanName, className, clazz));
            } else {
                beanDefinitionContext.add(assembleBeanDefinition(beanName, className, clazz));
            }

            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class itf : interfaces) {
                if (beanDefinitionContext.stream().anyMatch(ele -> ele.getClassName().equals(itf.getName()))) {
                    throw new RuntimeException("the Interface has multi implementers ." + itf.getName());
                }
                beanDefinitionContext.add(assembleBeanDefinition(itf.getName(), className, clazz));
            }

        }

        return beanDefinitionContext;
    }


    private String toLowerFirstCase(String source) {
        char[] arr = source.toCharArray();
        arr[0] = (char) (arr[0] + 32);
        return String.valueOf(arr);
    }

    private BeanDefinition assembleBeanDefinition(String beanName, String className, Class clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setClassName(className);
        beanDefinition.setFactoryBeanName(beanName);
        return beanDefinition;
    }

    private void loadConfig(String[] config) {
        applicationProperties = new Properties();
        try {
            applicationProperties.load(this.getClass().getResourceAsStream("/" + config[0].replace("classpath:", "")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void scannerPackage() {
        String scannerPath = applicationProperties.getProperty("scanner-package");
        String path = this.getClass().getClassLoader().getResource(scannerPath.replace("\\.", "/")).getPath();
        File file = new File(path);
        recurseClassFile(file);

        System.out.println(path);
    }


    private void recurseClassFile(File file) {
        if (file == null) {
            return;
        }

        String rootPath = this.getClass().getResource("/").getPath();
        for (File ele : file.listFiles()) {
            if (ele.isFile()) {
                if (!ele.getName().endsWith(".class")) continue;
                String filePath = "/" + ele.getAbsolutePath().replace("\\", "/");
                beanClassList.add(filePath.replace(rootPath, "").replace(".class", "").replace("/", "."));
                continue;
            }

            recurseClassFile(ele);
        }
    }


    public Properties getConfig() {
        return applicationProperties;
    }
}

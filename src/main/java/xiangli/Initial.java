package xiangli;

import xiangli.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Initial extends HttpServlet {
    private static final Properties config = new Properties();
    private static final List<String> ALL_CLASS = new ArrayList<>();
    private static final Map<String, Object> IOC = new HashMap<>();
    private static final Map<String, Method> HAND_MAPPING = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();
        String delPath = requestURI.replace(req.getContextPath(), "");
        Method method = HAND_MAPPING.get(delPath);
        Class<?> aClass = method.getDeclaringClass();


        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] paramsValue = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpServletRequest.class) {
                paramsValue[i] = req;
                continue;
            }

            if (parameterTypes[i] == HttpServletResponse.class) {
                paramsValue[i] = resp;
                continue;
            }

            if (parameterTypes[i] == String.class) {
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof XMParameter) {
                        XMParameter xmParameter = (XMParameter) annotation;
                        String name = xmParameter.name();
                        if (name != "") {
                            paramsValue[i] = req.getParameterValues(name)[0];
                        }
                    }
                }
            }
        }


        try {
            Object result = method.invoke(IOC.get(toLowerFirstCase(aClass.getSimpleName())), paramsValue);
            resp.getWriter().write(result.toString());
        } catch (Exception e) {
            try {
                resp.getWriter().write("500 error msg is " + e.getStackTrace().toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(requestURI);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            //加载配置文件
            loadConfig(config.getServletContext().getInitParameter("contextConfigLocation"));

            //扫描包
            doScanner();

            //实例化ioc
            loadIoCContext();

            //注入
            injection();

            //handle mapping
            generateHandMapping();

            System.out.println("init finished");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generateHandMapping() {
        if (IOC.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : IOC.entrySet()) {
            if (!entry.getValue().getClass().isAnnotationPresent(XMController.class)) {
                continue;
            }

            XMRequestMapping classMapping = entry.getValue().getClass().getAnnotation(XMRequestMapping.class);
            String baseUrl = classMapping != null ? classMapping.value() : "";

            Method[] methods = entry.getValue().getClass().getMethods();
            for (Method method : methods) {
                XMRequestMapping methodAnnotation = method.getAnnotation(XMRequestMapping.class);
                String methodUrl = methodAnnotation != null ? methodAnnotation.value() : "";
                HAND_MAPPING.put(baseUrl + methodUrl, method);
            }
        }
    }

    private void injection() throws IllegalAccessException {
        if (IOC.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : IOC.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(XMAutowired.class)) {
                    continue;
                }

                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isInterface()) {
                    field.set(entry.getValue(), IOC.get(type.getName()));
                    continue;
                }

                field.set(entry.getValue(), IOC.get(toLowerFirstCase(type.getSimpleName())));
            }

        }
    }


    private void loadIoCContext() throws Exception {
        if (ALL_CLASS.isEmpty()) {
            return;
        }

        for (String className : ALL_CLASS) {
            Class<?> clazz = Class.forName(className);
            if (!(clazz.isAnnotationPresent(XMController.class) || clazz.isAnnotationPresent(XMService.class)))
                continue;

            if (clazz.isAnnotationPresent(XMController.class)) {
                IOC.put(toLowerFirstCase(clazz.getSimpleName()), clazz.newInstance());
                continue;
            }

            XMService annotation = clazz.getAnnotation(XMService.class);
            String alias = annotation.value();
            if (alias != "") {
                IOC.put(toLowerFirstCase(clazz.getSimpleName()), clazz.newInstance());
            } else {
                IOC.put(alias, clazz.newInstance());
            }

            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class itf : interfaces) {
                if (IOC.containsKey(itf.getName())) {
                    throw new RuntimeException("the Interface has multi implementers ." + itf.getName());
                }
                IOC.put(itf.getName(), clazz.newInstance());
            }

        }

    }

    private String toLowerFirstCase(String source) {
        char[] arr = source.toCharArray();
        arr[0] = (char) (arr[0] + 32);
        return String.valueOf(arr);
    }

    private void doScanner() {
        String scannPath = config.getProperty("scanner-package");
        String path = this.getClass().getClassLoader().getResource(scannPath.replace("\\.", "/")).getPath();
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
                ALL_CLASS.add(filePath.replace(rootPath, "").replace(".class", "").replace("/", "."));
                continue;
            }

            recurseClassFile(ele);
        }
    }


    private void loadConfig(String contextConfigLocation) throws IOException {
        InputStream is = this.getClass().getResourceAsStream("/" + contextConfigLocation.split(":")[1]);
        config.load(is);
    }


}

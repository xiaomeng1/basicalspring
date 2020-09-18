package xiangliV2.webmvc.servlet;

import xiangliV2.annotation.XMController;
import xiangliV2.annotation.XMRequestMapping;
import xiangliV2.context.XMApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class XMDispatchServlet extends HttpServlet {

    private static final List<XMHandleMapping> handleMappingCache = new ArrayList<>();

    private static final Map<XMHandleMapping, XMHandleAdapter> handleAdapterCache = new HashMap<>();

    private static final List<XMViewResolver> viewResolerCache = new ArrayList<>();

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();
        String delPath = requestURI.replace(req.getContextPath(), "");
        XMHandleMapping handle = getHandleMapping(delPath);
        if (handle == null) {
            processModelAndView(new XMModelAndView("500", null), req, resp);
        }

        XMHandleAdapter xmHandleAdapter = handleAdapterCache.get(handle);
        XMModelAndView modelAndView = xmHandleAdapter.handle(req, resp);
        processModelAndView(modelAndView, req, resp);
    }


    private void processModelAndView(XMModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        XMViewResolver xmViewResolver = viewResolerCache.get(0);
        XMView xmView = xmViewResolver.resolverToView(modelAndView.getViewName());

        xmView.render(modelAndView.getModelMap(), request, response);
    }


    private XMHandleMapping getHandleMapping(String url) {
        return handleMappingCache.stream().filter(handle -> handle.getUrl().matcher(url).find()).findFirst().get();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化 XMApplicationContext
        XMApplicationContext applicationContext = new XMApplicationContext(config.getServletContext().getInitParameter("contextConfigLocation"));

        //init 组件
        initStrategy(applicationContext);
    }


    private void initStrategy(XMApplicationContext applicationContext) {
        //初始化handle mapping
        initHandleMapping(applicationContext);

        //初始化HandleAdapter 将handle mapping 适配到方法的参数赋值正常调用 供 ViewResolver解析
        initHandleAdapter(applicationContext);

        //初始化ViewResolver
        initViewResolver(applicationContext);
    }

    private void initViewResolver(XMApplicationContext applicationContext) {
        Properties config = applicationContext.getConfig();
        String home = config.getProperty("template.home");
        viewResolerCache.add(new XMViewResolver(home));
    }


    private void initHandleAdapter(XMApplicationContext applicationContext) {
        handleMappingCache.stream().forEach(handleMapping ->
                handleAdapterCache.put(handleMapping, new XMHandleAdapter(handleMapping))
        );
    }

    private void initHandleMapping(XMApplicationContext applicationContext) {
        List<Method> objectMethod = Arrays.asList(Object.class.getMethods());
        if (applicationContext.getBeanDefinitionCount() == 0) {
            return;
        }

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object instance = applicationContext.getBean(beanName);
            if (!instance.getClass().isAnnotationPresent(XMController.class)) {
                continue;
            }

            XMRequestMapping classMapping = instance.getClass().getAnnotation(XMRequestMapping.class);
            String baseUrl = classMapping != null ? classMapping.value() : "";

            Method[] methods = instance.getClass().getMethods();
            for (Method method : methods) {
                if (objectMethod.contains(method)) {
                    continue;
                }
                XMRequestMapping methodAnnotation = method.getAnnotation(XMRequestMapping.class);
                String methodUrl = methodAnnotation != null ? methodAnnotation.value() : "";
                String regex = (baseUrl + methodUrl).replace("*", ".*");
                handleMappingCache.add(new XMHandleMapping(Pattern.compile(regex), method, instance));
            }
        }
    }
}

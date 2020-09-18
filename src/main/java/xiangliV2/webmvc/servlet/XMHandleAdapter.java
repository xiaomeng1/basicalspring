package xiangliV2.webmvc.servlet;

import xiangliV2.annotation.XMParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class XMHandleAdapter {

    private XMHandleMapping handleMapping;

    public XMHandleAdapter(XMHandleMapping handleMapping) {
        this.handleMapping = handleMapping;
    }

    public XMModelAndView handle(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String[]> parameterMap = request.getParameterMap();

        //参数赋值
        Method method = handleMapping.getMethod();

        Class<?>[] parameterTypes = method.getParameterTypes();
        //获取所有注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        //定义一个参数数组
        Object[] methodParameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpServletRequest.class) {
                methodParameters[i] = request;
                continue;
            }

            if (parameterTypes[i] == HttpServletResponse.class) {
                methodParameters[i] = response;
                continue;
            }

            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (int j = 0; j < parameterAnnotation.length; j++) {
                if (parameterAnnotation[j] instanceof XMParameter) {
                    String value = ((XMParameter) parameterAnnotation[i]).name();
                    if (!"".equals(value)) {
                        methodParameters[i] = convertToType(parameterMap.get(value)[0], parameterTypes[i]);
                    }
                }
            }
        }

        try {
            Object result = handleMapping.getMethod().invoke(handleMapping.getInstance(), methodParameters);
            if (result == null || result instanceof Void) {
                return null;
            }

            if (result instanceof XMModelAndView) {
                return (XMModelAndView) result;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> T convertToType(String source, Class<T> parameterType) {
        if (parameterType == String.class) {
            return (T) source;
        }

        if (parameterType == Integer.class) {
            return (T) Integer.valueOf(source);
        }

        return null;
    }
}

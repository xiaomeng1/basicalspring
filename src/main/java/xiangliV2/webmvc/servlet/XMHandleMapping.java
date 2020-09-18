package xiangliV2.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class XMHandleMapping {

    private Pattern url;

    private Method method;

    private Object instance;


    public XMHandleMapping(Pattern url, Method method, Object instance) {
        this.url = url;
        this.method = method;
        this.instance = instance;
    }

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}

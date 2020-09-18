package xiangliV2.webmvc.servlet;

import java.util.Map;

public class XMModelAndView {

    private String viewName;

    private Map<String, ?> modelMap;

    public XMModelAndView(String viewName, Map<String, ?> modelMap) {
        this.viewName = viewName;
        this.modelMap = modelMap;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map<String, ?> modelMap) {
        this.modelMap = modelMap;
    }
}

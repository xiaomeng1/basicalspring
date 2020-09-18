package xiangliV2.webmvc.servlet;

import java.io.File;

public class XMViewResolver {


    private String viewHome;


    public XMViewResolver(String viewHome) {
        this.viewHome = viewHome;
    }

    public XMView resolverToView(String viewName) {
        String viewHome = this.getClass().getClassLoader().getResource(this.viewHome).getFile();
        File file = new File(String.format("%s/%s", viewHome, viewName));

        return new XMView(file);
    }

    public String getViewHome() {
        return viewHome;
    }

    public void setViewHome(String viewHome) {
        this.viewHome = viewHome;
    }

}

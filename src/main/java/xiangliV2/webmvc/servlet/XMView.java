package xiangliV2.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMView {

    private File view;

    public XMView(File view) {
        this.view = view;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        try {
            RandomAccessFile accessFile = new RandomAccessFile(view, "r");
            String line = null;
            while ((line = accessFile.readLine()) != null) {
                line = new String(line.getBytes("ISO-8859-1"), "utf-8");
                String regex = "￥\\{(.*)\\}";
                Matcher matcher = Pattern.compile(regex).matcher(line);
                if (matcher.find()) {
                    Object value = model.get(matcher.group(1));
                    line = line.replaceFirst(regex, value.toString());
                }
                sb.append(line);
            }

            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

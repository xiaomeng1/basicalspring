package xiangliV2.webmvc.servlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

    public static void main(String[] args) {
        String line = "大家好，我是￥{teacher}老师<br/>欢迎大家一起来探索Spring的世界";
        String regex = "￥\\{(.*)\\}";
        Matcher matcher1 = Pattern.compile(regex).matcher(line);
        if(matcher1.find()) {

            System.out.println(matcher1.group(1));
        }

        Matcher matcher = Pattern.compile(regex).matcher(line);
//        String group = matcher.group();
    }
}

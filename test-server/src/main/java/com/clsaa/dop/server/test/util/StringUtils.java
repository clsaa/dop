package com.clsaa.dop.server.test.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author xihao
 * @version 1.0
 * @since 12/04/2019
 */
@Slf4j
public class StringUtils {

    private static String dollarRegex = "\\S*\\$\\{\\S+\\}\\S*";
    private static Pattern dollarPattern = Pattern.compile(dollarRegex);

    private static String braceRegex = "\\S*\\{\\S+\\}\\S*";
    private static Pattern bracePattern = Pattern.compile(braceRegex);

    // 带有${}变量的字符串, 进行变量替换
    public static String tryToResolveDollar(String origin, Map<String, String> data) {
        if (dollarPattern.matcher(origin).matches()) {
            try {
                Template template = new Template("tpl", origin, new Configuration(Configuration.VERSION_2_3_28));
                StringWriter resultWriter = new StringWriter();
                template.process(data, resultWriter);
                return resultWriter.toString();
            } catch (IOException | TemplateException e) {
                throw new RuntimeException(
                        String.format("[resolve EL string with param error!]: origin: %s, error message: %s",
                                origin, e.getMessage())
                );
            }
        }
        return origin;
    }

    // 带有{}变量的字符串，进行变量替换
    public static String tryToResolveBrace(String origin, Map<String, String> data) {
        while (bracePattern.matcher(origin).matches()) {
            char[] chars = origin.toCharArray();
            int a = -1, b = -1;
            find:
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '{') {
                    for (int j = i + 1; j < chars.length; j++) {
                        if (chars[j] == '}') {
                            a = i + 1;
                            b = j;
                            break find;
                        }
                    }
                }
            }

            if (a != -1) {
                String ref = origin.substring(a, b);
                String value = data.getOrDefault(ref, String.format("Path Param %s missed", ref));
                origin = new String(chars, 0, a - 1) +
                        value +
                        new String(chars, b + 1, chars.length - b - 1);
            }
        }
        return origin;
    }

    public static void main(String[] args) {
        String origin = "{p1}/{p2}";
        Map<String, String> map = new HashMap<>();
        map.put("p1", "a");
        map.put("p2", "b");
        System.out.println(tryToResolveBrace(origin, map));
    }
}

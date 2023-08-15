package AntMatch;

import lombok.ToString;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/15 18:44
 */
public class AntMatchTest {

    private static final PathMatcher MATCHER = new AntPathMatcher();


    private static void match(PathMatcher matcher,String pattern,String reqPath){
        boolean match = matcher.match(pattern, reqPath);
        System.out.println(" pattern = " + pattern + " ,reqPath = " + reqPath + " ,match = " + match);
    }

    private static void extractUriTemplateVariables(PathMatcher matcher,String pattern,String reqPath){
        Map<String, String> variablesMap = matcher.extractUriTemplateVariables(pattern, reqPath);
        System.out.println("variablesMap = " + variablesMap);
    }



    @Test
    public void test01(){
        System.out.println("====测试？匹配单个字符串====");
        String pattern = "/a?c";
        match(MATCHER,pattern,"/abc");
        match(MATCHER,pattern,"/abdc");
        match(MATCHER,pattern,"/ac");
    }


    @Test
    public void test02(){
        System.out.println("====测试*匹配多个字符串====");
        String pattern = "/a*c";
        match(MATCHER,pattern,"/abc");
        match(MATCHER,pattern,"/a/c");
        match(MATCHER,pattern,"/ac");
    }


    @Test
    public void test03(){
        System.out.println("====测试**匹配多个字符串====");
        String pattern = "/a/**/c";
        match(MATCHER,pattern,"/abc");
        match(MATCHER,pattern,"/a//c");
        match(MATCHER,pattern,"/ac");
    }

    @Test
    public void test5() {
        System.out.println("======={pathVariable:可选的正则表达式}=======");
        String pattern = "/api/yourbatman/{age}";

        match( MATCHER, pattern, "/api/yourbatman/10");
        match( MATCHER, pattern, "/api/yourbatman/Ten");

        // 打印提取到的内容
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/10");
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/Ten");
    }

    @Test
    public void test6() {
        System.out.println("======={pathVariable:可选的正则表达式}=======");
        String pattern = "/api/yourbatman/{age:[0-9]*}";

        match( MATCHER, pattern, "/api/yourbatman/10");
        match( MATCHER, pattern, "/api/yourbatman/Ten");

        // 打印提取到的内容
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/10");
        // 可以使用正则的方式来是实现对于路径的约束
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/Ten");
    }
}

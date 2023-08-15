package org.example;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Map;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/15 16:39
 */
public class AntMatch {
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static void main(String[] args) {
        
    }


    private static void match(int index,PathMatcher matcher,String pattern,String reqPath){
        boolean match = matcher.match(pattern, reqPath);
        System.out.println("index = " + index + " pattern = " + pattern + " reqPath = " + reqPath + " match = " + match);
    }
    
    private static void extractUriTemplateVariables(PathMatcher matcher,String pattern,String reqPath){
        Map<String, String> variablesMap = matcher.extractUriTemplateVariables(pattern, reqPath);
        System.out.println("variablesMap = " + variablesMap);
    }
}


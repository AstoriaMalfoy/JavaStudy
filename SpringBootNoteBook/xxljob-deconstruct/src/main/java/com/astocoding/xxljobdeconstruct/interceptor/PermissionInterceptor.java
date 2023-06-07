package com.astocoding.xxljobdeconstruct.interceptor;

import com.astocoding.xxljobdeconstruct.anno.PermissionLimit;
import com.astocoding.xxljobdeconstruct.constant.Constant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Objects;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/17 17:06
 */
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        boolean needLogin = false;
        boolean needAdminuser = false;
        HandlerMethod method = (HandlerMethod) handler;
        PermissionLimit methodAnnotation = method.getMethodAnnotation(PermissionLimit.class);
        if (methodAnnotation != null){
            needLogin = methodAnnotation.limit();
            needAdminuser = methodAnnotation.adminuser();
        }

        if (needLogin) {
            Cookie identityCookie = getCookieWithName(request, Constant.DEMO_USER_IDENTITY);
            if (identityCookie == null) {
                response.sendRedirect("/index.html");
                return false;
            }
            String value = identityCookie.getValue();
            if (value == null || value.length() == 0) {
                response.sendRedirect("/index.html");
                return false;
            }
            String[] split = value.split(":");
            String username = split[0];
            String password = split[1];
            if (needAdminuser){
                if (!Objects.equals(username, "admin") || !Objects.equals(password, "admin")){
                    response.sendRedirect("/index.html");
                    return false;
                }
            }else{
                if (Objects.equals(username,password)){
                    return true;
                }else{
                    response.sendRedirect("/index.html");
                    return false;
                }
            }
        }
        return true;
    }

    private Cookie getCookieWithName(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)){
                return cookie;
            }
        }
        return null;
    }
}

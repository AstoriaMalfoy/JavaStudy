package com.astocoding.xxljobdeconstruct.controller;

import com.astocoding.xxljobdeconstruct.anno.PermissionLimit;
import com.astocoding.xxljobdeconstruct.constant.Constant;
import com.astocoding.xxljobdeconstruct.model.HttpReturn;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/17 10:58
 */

@RestController
public class WriteCookie {

    @Data
    public static class LoginRequest {
        private String name;
        private String password;
        private boolean rememberMe;
    }

    @PostMapping("/login")
    public HttpReturn testSetCookie(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        setCookie(response, loginRequest.getName(), loginRequest.getPassword(), loginRequest.isRememberMe());
        return HttpReturn.success("success");
    }


    public void setCookie(HttpServletResponse response, String name, String password, boolean rememberMe) {
        String testCooke = name + ":" + password;
        Cookie cookie = new Cookie(Constant.DEMO_USER_IDENTITY, testCooke);
        if (rememberMe) {
            cookie.setMaxAge(Integer.MAX_VALUE);
        } else {
            cookie.setMaxAge(-1);
        }
        response.addCookie(cookie);
    }


    @PostMapping("/echo")
    @PermissionLimit(adminuser = true)
    public HttpReturn getEchoInfo(String message) {
        return HttpReturn.success(message);
    }
}

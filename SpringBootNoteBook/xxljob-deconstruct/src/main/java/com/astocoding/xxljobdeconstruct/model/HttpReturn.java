package com.astocoding.xxljobdeconstruct.model;

import java.io.Serializable;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/17 11:11
 */
public class HttpReturn<T> implements Serializable {


    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static <T> HttpReturn<T> success(T content){
        return new HttpReturn<>(SUCCESS_CODE,"success",content);
    }

    public static <T> HttpReturn<T> fail(T content,String message){
        return new HttpReturn<>(FAIL_CODE,message,content);
    }

    public static <T> HttpReturn<T> fail(String message){
        return new HttpReturn<>(FAIL_CODE,message,null);
    }

    private int code;
    private String message;
    private T content;

    public HttpReturn(int code,String message,T content){
        this.code = code;
        this.message = message;
        this.content = content;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "HttpReturn{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    public void setContent(T content) {
        this.content = content;
    }
}

package com.astocoding.xxljobdeconstruct.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/17 17:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionLimit {
    boolean limit() default true;
    boolean adminuser() default false;
}

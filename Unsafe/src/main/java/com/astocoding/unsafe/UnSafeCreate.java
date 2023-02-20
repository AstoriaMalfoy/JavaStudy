package com.astocoding.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/16 16:45
 *
 * 创建Unsafe类，但是在通产情况下不应该使用此列，此类设计的目的只是为了满足框架的设计者能够使用更加底层的操作方式，
 * 在通常的开发场景化中，不建议使用类来进行内存的申请和分配操作
 * 使用Unsafe类申请的内存空间并不会被JVM动态维护，需要自己手动维护，手动释放
 */
public class UnSafeCreate {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        new UnSafeCreate().normalCreateUnSafeObject();
    }


    /**
     * 如果使用方法Unsafe.getUnsafe()要获取Unsafe类时候，会抛出异常 Exception in thread "main" java.lang.SecurityException: Unsafe
     * 这是因为调用Unsafe.getUnsafe()方法的类必须是从 “引导类加载器（BootStrapLoader）” 加载， 而默认的应用的类加载器是使用的 “应用类加载器（AppClassLoader）”
     *
     * 解决方案由两种 一种是在JVM启动的时候添加参数，将当前类添加到JVM的 “受信任域”
     * 另一种是直接通过反射来获取
     *
     *
     */
    public void normalCreateUnSafeObject(){
        Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println(unsafe);
    }


    /**
     * 通过反射强制获取
     */
    public void reflexetNormallSetObject() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafes = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafes.setAccessible(true);
        System.out.println(theUnsafes.get("theUnsafe"));
    }

}

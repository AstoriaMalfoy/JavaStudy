package com.astocoding.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/16 17:41
 */
public class UnsafeBase {
    public static Unsafe getUnsafeObject() {
        try{
            Field theUnsafe =
                    Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get("theUnsafe");
        }catch (Exception e){
            return null;
        }
    }

    public static Unsafe getUnsafe(){
        System.out.println("[warn] Unsafe.getUnsafe() is not supported in this environment");
        return Unsafe.getUnsafe();
    }

}

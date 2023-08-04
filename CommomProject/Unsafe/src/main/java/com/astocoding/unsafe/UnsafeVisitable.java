package com.astocoding.unsafe;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.BB_Result;
import org.openjdk.jcstress.infra.results.B_Result;
import org.openjdk.jcstress.infra.results.I_Result;
import sun.misc.Unsafe;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/20 10:20\
 *
 *
 * 如果想要对非volatile变量，通过unsafe想要实现同volatile的访问效果，set和get需要同时使用，进而同时保证 可见性和 有序性\
 * 如果是单独在set或者get的时候使用，那么就无法实现同volatile的访问效果，如果只是在设置的时候使用volatile方式，无法保证再在使用的时候是从内存中读取的
 * 如果单独在set的时候使用，同样也无法保证修改被瞬间写入到内存
 *
 * 使用Volatile能够确保可见性和有序性，在Unsafe类中还有一种写入方式是 putOrderObject , 该操作只能单纯的确保有序性，开销要比volatile小
 */
@JCStressTest()
@State
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "respectResult")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "unRespectResult")
@Outcome(id = "133", expect = Expect.ACCEPTABLE, desc = "testAccess")
public class UnsafeVisitable {
    private int a = 0;
    private boolean flag = false;

    private static Unsafe unsafe = UnsafeBase.getUnsafeObject();
    private static long aOffset;
    private static long flagOffset;

    static {
        try {
            aOffset = unsafe.objectFieldOffset(UnsafeVisitable.class.getDeclaredField("a"));
            flagOffset = unsafe.objectFieldOffset(UnsafeVisitable.class.getDeclaredField("flag"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Actor
    public void method(I_Result i_result) {
        if (flag) {
            i_result.r1 = unsafe.getIntVolatile(this,aOffset) * 2; // normal result 4
        } else {
            i_result.r1 = 1; // normal result 1
        }
    }


    @Actor
    public void visitable(I_Result i_result) {
        // 防止发生指令重排
        unsafe.putIntVolatile(this,aOffset,2);
        unsafe.putBooleanVolatile(this,flagOffset,true);
//        this.a = 2;
//        this.flag = true;
    }

}





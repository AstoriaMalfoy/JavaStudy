package com.astocoding.unsafe;

import sun.misc.Unsafe;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/28 11:58
 */
public class Park {
    // unsafe instance
    private static Unsafe unsafe = UnsafeBase.getUnsafeObject();


    /**
     * park
     * 第一个参数代表是否是绝对时间，如果为true，会使用ms定时，如果为false，会使用ns定时
     * 第二个参数是具体的时间
     */
    public void park() {
        unsafe.park(false, 0L);
    }

    public static void main(String[] args) {
        Park park = new Park();
        park.park();
    }
}

package com.astocoding.unsafe;

import sun.misc.Unsafe;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/16 17:41
 */
public class GetFieldOffset {

    private Integer integerField;       //  integerField 36

    private Boolean booleanField;       //  booleanField 40

    private String strField;            //  strField 44

    private Long longFiled;             //  longFiled 48

    private Double doubleField;         //  doubleField 52

    private Short shortField;           //  shortField 56

    private byte byteField;             //  byteField 32

    private int uintField;              //  uintField 12

    private long ulongField;            //  ulongField 16

    private double udoubleField;        //  udoubleField 24

    private static Unsafe unsafe = UnsafeBase.getUnsafeObject();


    /**
     * unsafe.objectFieldOffset 方法用于获取某个属性在其类中编排时候的相对位置，这个位置也是运行时期的相关属性的内存偏移量
     */

    public static void main(String[] args) {


        try {
            System.out.println("integerField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("integerField")));
            System.out.println("booleanField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("booleanField")));
            System.out.println("strField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("strField")));
            System.out.println("longFiled " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("longFiled")));
            System.out.println("doubleField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("doubleField")));
            System.out.println("shortField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("shortField")));
            System.out.println("byteField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("byteField")));
            System.out.println("uintField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("uintField")));
            System.out.println("ulongField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("ulongField")));
            System.out.println("udoubleField " + unsafe.objectFieldOffset(GetFieldOffset.class.getDeclaredField("udoubleField")));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}

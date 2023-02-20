package com.astocoding.unsafe;


import lombok.Data;
import lombok.ToString;
import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;
import sun.misc.Unsafe;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/18 16:00
 */
public class ModifyFields {
    public static void main(String[] args) throws NoSuchFieldException {
        new ModifyFields().testWriteMemory();
    }

    /**
     * 针对内存的普通读写操作
     * Unsafe可以读取一个列的内存的属性，无论他的属性是共有的还是私有的，并且能够对这个属性进行读写
     * Unsafe.getInt(Object obj,long offset)
     * Unsafe.putInt(Object obj,long offset,int value)
     * 除了int类型之外 boolean,byte,short,long,float,double 类型都支持
     * 但是最常用的是 int 因为在Java中 对象的内存地址是4个字节，和int类型的长度一样，所以除了获取内存中的int类型值之外，还可以看作获取某个属性的内存地址
     *
     * @see UnsafeVisitable
     * 除了上述支持的普通的修改变量的值以外，还支持volatile模式的访问，如果需要访问的变量本身没有使用volatile关键字，又需要在并发环境下访问，
     * 那么就可以使用unsafe下getIntVolatile和putVolatile来进行读写，
     */
    private void testWriteMemory() throws NoSuchFieldException {
        Person personA = Person.of("teat_a", 12);
        Person personB = Person.of("test_b", 13);
        Unsafe unsafe = UnsafeBase.getUnsafeObject();
        assert unsafe != null;


        // name offset
        long nameOffset = unsafe.objectFieldOffset(Person.class.getDeclaredField("name"));
        // age offset
        long ageOffset = unsafe.objectFieldOffset(Person.class.getDeclaredField("age"));

        System.out.println("before swap :");
        System.out.println("personA :" + personA);
        System.out.println("personB :" + personB);

        // swap
        // 针对于类，获取的是对象的实际内存位置
        int pointA_name = unsafe.getInt(personA,nameOffset);
        int pointA_age = unsafe.getInt(personA,ageOffset);


        // 针对于基本数据类型，获取的就是实际的值
        int pointB_name = unsafe.getInt(personB,nameOffset);
        int pointB_age = unsafe.getInt(personB,ageOffset);

        unsafe.putInt(personA,nameOffset,pointB_name);
        unsafe.putInt(personA,ageOffset,pointB_age);

        unsafe.putInt(personB,nameOffset,pointA_name);
        unsafe.putInt(personB,ageOffset,pointA_age);

        System.out.println("after swap :");
        System.out.println("personA :" + personA);
        System.out.println("personB :" + personB);

        System.out.println("------------------");
        System.out.println("pointA_name :" + pointA_name);
        System.out.println("pointA_age :" + pointA_age);
        System.out.println("pointB_name :" + pointB_name);
        System.out.println("pointB_age :" + pointB_age);


    }


    @Data
    @ToString
    public static class Person{
        private String name;
        private int age;
        private Person(String name,int age){
            this.name = name;
            this.age = age;
        }
        public static Person of(String name,int age){
            return new Person(name,age);
        }
    }



}

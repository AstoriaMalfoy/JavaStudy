package com.astocoding.unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import sun.misc.Unsafe;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/21 15:08
 *
 * 可以创建对象，并且不会调用任何的构造函数 allocateInstance(Class<?> clz)
 */
public class InstanceObject {

    private static Unsafe unsafe = UnsafeBase.getUnsafeObject();

    private static int baseId = 1;

    public static void main(String[] args) throws InstantiationException {
        for (int i=0;i<10;i++){
            System.out.println(
                    unsafe.allocateInstance(DemoClass.class)
            );
        }
    }

    @Data
    @ToString
    public static class DemoClass {
        private int age;
        private String str;
        private final int id;

        public DemoClass(int age,String str){
            System.out.println("invoke the all args constructor");
            this.age = age;
            this.str = str;
            this.id = baseId ;
            baseId ++;
        }

        public DemoClass(){
            System.out.println("invoke the no args constructor");
            this.id = baseId;
            baseId ++;
        }


        public static DemoClass of(int age, String str) {
            return new DemoClass(age,str);
        }
    }

}



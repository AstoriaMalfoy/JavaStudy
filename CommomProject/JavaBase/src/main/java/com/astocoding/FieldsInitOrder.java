package com.astocoding;

import java.sql.Statement;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/16 14:17
 */
public class FieldsInitOrder {

    static {
        System.out.println("now call the static block");
    }
    private String str = getString();

    public FieldsInitOrder(){
        System.out.println("now call the constructor");
    }

    private String getString(){
        System.out.println("now call the getString method");
        return str;
    }

    public static void main(String[] args) {
        FieldsInitOrder fieldsInitOrder = new FieldsInitOrder();
    }
}

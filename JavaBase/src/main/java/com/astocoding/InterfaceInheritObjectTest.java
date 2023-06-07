package com.astocoding;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/4/1 10:51
 */
public class InterfaceInheritObjectTest {

    public static byte[] dump() throws Exception {

        ClassWriter classWriter = new ClassWriter(0);

        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE, "com/astocoding/inte/DemoInterface", null, "java/lang/Object", null);

        classWriter.visitSource("DemoInterface.java", null);

        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.asList(dump()));
    }
}

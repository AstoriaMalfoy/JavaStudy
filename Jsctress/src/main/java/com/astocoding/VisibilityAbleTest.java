package com.astocoding;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;
/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/18 17:22
 */


/**
 * JSCressTest : 标注该类是一个并发测试的类，参数是一个Mode，该参数有两个值
 *  Mode.Termination:
 *  Mode.Continuous:
 */
@JCStressTest()
@State
@Outcome(id = {"1","4"} , expect = Expect.ACCEPTABLE,desc = "respectResult")
@Outcome(id = "0",expect = Expect.ACCEPTABLE_INTERESTING,desc = "这不是期望的结果")
public class VisibilityAbleTest {
    private int a = 0;
    private boolean flag = false;

    @Actor
    public void method(I_Result result){
        if (flag){
            result.r1 = a * 2;
        }else{
            result.r1 = 1;
        }
    }

    @Actor
    public void method2(I_Result result){
        a = 2;
        flag = true;
    }
}

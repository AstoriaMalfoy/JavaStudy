package com.astocoding;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;


/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/20 15:13
 */
@JCStressTest
@State
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "respectResult")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "unRespectResult")
@Outcome(id = "133", expect = Expect.ACCEPTABLE, desc = "testAccess")
public class SimpleTest {
    int v = 0;
    boolean flag = false;

    @Actor
    public void actor1(I_Result result) {
        if (flag){
            result.r1 = v++;
        }else{
            result.r1 = 2 + v;
        }
    }
    @Actor
    public void actor2(I_Result result) {
        if (v == 0){
            flag = false;
        }

    }

}






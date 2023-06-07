package com.astocoding;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/20 17:41
 */
@JCStressTest
@Outcome(id = "1, 1",expect = Expect.ACCEPTABLE_INTERESTING,desc = "same value")
@Outcome(id = "1, 2",expect = Expect.ACCEPTABLE,desc = "actor1")
@Outcome(id = "2, 1",expect = Expect.ACCEPTABLE,desc = "actor2")
@State
public class API_SIMPLE {

    int v ;

    @Actor
    public void methodA(II_Result r){
        r.r1 = ++v;
    }

    @Actor
    public void methodB(II_Result r){
        r.r2 = ++v;
    }

}

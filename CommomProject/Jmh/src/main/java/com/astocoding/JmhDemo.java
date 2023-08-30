package com.astocoding;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@Fork(2)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 10)
@Measurement(time = 5,timeUnit = TimeUnit.SECONDS)
public class JmhDemo {

    @Benchmark
    public String stringAddr() {
        String string = "";
        for (int i = 0; i < 1000; i++) {
            string = string + i;
        }
        return string;
    }

    @Benchmark
    public String stringBuilderAdd() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }



    @Test
    @SneakyThrows
    public void testRun(){
        Options options = new OptionsBuilder()
                .include(JmhDemo.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

}

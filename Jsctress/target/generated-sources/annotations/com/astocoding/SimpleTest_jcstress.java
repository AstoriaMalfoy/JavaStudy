package com.astocoding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.openjdk.jcstress.infra.runners.TestConfig;
import org.openjdk.jcstress.infra.collectors.TestResultCollector;
import org.openjdk.jcstress.infra.runners.Runner;
import org.openjdk.jcstress.infra.runners.StateHolder;
import org.openjdk.jcstress.util.Counter;
import org.openjdk.jcstress.vm.WhiteBoxSupport;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.Collections;
import java.util.List;
import com.astocoding.SimpleTest;
import org.openjdk.jcstress.infra.results.I_Result_jcstress;

public class SimpleTest_jcstress extends Runner<I_Result_jcstress> {

    volatile StateHolder<SimpleTest, I_Result_jcstress> version;

    public SimpleTest_jcstress(TestConfig config, TestResultCollector collector, ExecutorService pool) {
        super(config, collector, pool, "com.astocoding.SimpleTest");
    }

    @Override
    public Counter<I_Result_jcstress> sanityCheck() throws Throwable {
        Counter<I_Result_jcstress> counter = new Counter<>();
        sanityCheck_API(counter);
        sanityCheck_Footprints(counter);
        return counter;
    }

    private void sanityCheck_API(Counter<I_Result_jcstress> counter) throws Throwable {
        final SimpleTest s = new SimpleTest();
        final I_Result_jcstress r = new I_Result_jcstress();
        Collection<Future<?>> res = new ArrayList<>();
        res.add(pool.submit(() -> s.actor1(r)));
        res.add(pool.submit(() -> s.actor2(r)));
        for (Future<?> f : res) {
            try {
                f.get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }
        counter.record(r);
    }

    private void sanityCheck_Footprints(Counter<I_Result_jcstress> counter) throws Throwable {
        config.adjustStrides(size -> {
            version = new StateHolder<>(new SimpleTest[size], new I_Result_jcstress[size], 2, config.spinLoopStyle);
            for (int c = 0; c < size; c++) {
                I_Result_jcstress r = new I_Result_jcstress();
                SimpleTest s = new SimpleTest();
                version.rs[c] = r;
                version.ss[c] = s;
                s.actor1(r);
                s.actor2(r);
                counter.record(r);
            }
        });
    }

    @Override
    public Counter<I_Result_jcstress> internalRun() {
        version = new StateHolder<>(new SimpleTest[0], new I_Result_jcstress[0], 2, config.spinLoopStyle);

        control.isStopped = false;

        List<Callable<Counter<I_Result_jcstress>>> tasks = new ArrayList<>();
        tasks.add(this::actor1);
        tasks.add(this::actor2);
        Collections.shuffle(tasks);

        Collection<Future<Counter<I_Result_jcstress>>> results = new ArrayList<>();
        for (Callable<Counter<I_Result_jcstress>> task : tasks) {
            results.add(pool.submit(task));
        }

        try {
            TimeUnit.MILLISECONDS.sleep(config.time);
        } catch (InterruptedException e) {
        }

        control.isStopped = true;

        waitFor(results);

        Counter<I_Result_jcstress> counter = new Counter<>();
        for (Future<Counter<I_Result_jcstress>> f : results) {
            try {
                counter.merge(f.get());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
        return counter;
    }

    public final void jcstress_consume(StateHolder<SimpleTest, I_Result_jcstress> holder, Counter<I_Result_jcstress> cnt, int a, int actors) {
        SimpleTest[] ss = holder.ss;
        I_Result_jcstress[] rs = holder.rs;
        int len = ss.length;
        int left = a * len / actors;
        int right = (a + 1) * len / actors;
        for (int c = left; c < right; c++) {
            I_Result_jcstress r = rs[c];
            SimpleTest s = ss[c];
            ss[c] = new SimpleTest();
            cnt.record(r);
            r.r1 = 0;
        }
    }

    public final void jcstress_updateHolder(StateHolder<SimpleTest, I_Result_jcstress> holder) {
        if (!holder.tryStartUpdate()) return;
        SimpleTest[] ss = holder.ss;
        I_Result_jcstress[] rs = holder.rs;
        int len = ss.length;

        int newLen = holder.updateStride ? Math.max(config.minStride, Math.min(len * 2, config.maxStride)) : len;

        SimpleTest[] newS = ss;
        I_Result_jcstress[] newR = rs;
        if (newLen > len) {
            newS = Arrays.copyOf(ss, newLen);
            newR = Arrays.copyOf(rs, newLen);
            for (int c = len; c < newLen; c++) {
                newR[c] = new I_Result_jcstress();
                newS[c] = new SimpleTest();
            }
         }

        version = new StateHolder<>(control.isStopped, newS, newR, 2, config.spinLoopStyle);
        holder.finishUpdate();
   }

    public final Counter<I_Result_jcstress> actor1() {

        Counter<I_Result_jcstress> counter = new Counter<>();
        while (true) {
            StateHolder<SimpleTest,I_Result_jcstress> holder = version;
            if (holder.stopped) {
                return counter;
            }

            SimpleTest[] ss = holder.ss;
            I_Result_jcstress[] rs = holder.rs;
            int size = ss.length;

            holder.preRun();

            for (int c = 0; c < size; c++) {
                SimpleTest s = ss[c];
                I_Result_jcstress r = rs[c];
                r.trap = 0;
                s.actor1(r);
            }

            holder.postRun();

            jcstress_consume(holder, counter, 0, 2);
            jcstress_updateHolder(holder);

            holder.postUpdate();
        }
    }

    public final Counter<I_Result_jcstress> actor2() {

        Counter<I_Result_jcstress> counter = new Counter<>();
        while (true) {
            StateHolder<SimpleTest,I_Result_jcstress> holder = version;
            if (holder.stopped) {
                return counter;
            }

            SimpleTest[] ss = holder.ss;
            I_Result_jcstress[] rs = holder.rs;
            int size = ss.length;

            holder.preRun();

            for (int c = 0; c < size; c++) {
                SimpleTest s = ss[c];
                I_Result_jcstress r = rs[c];
                r.trap = 0;
                s.actor2(r);
            }

            holder.postRun();

            jcstress_consume(holder, counter, 1, 2);
            jcstress_updateHolder(holder);

            holder.postUpdate();
        }
    }

}

package com.astocoding.unsafe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openjdk.jcstress.infra.runners.ForkedTestConfig;
import org.openjdk.jcstress.infra.collectors.TestResult;
import org.openjdk.jcstress.infra.runners.Runner;
import org.openjdk.jcstress.infra.runners.WorkerSync;
import org.openjdk.jcstress.util.Counter;
import org.openjdk.jcstress.os.AffinitySupport;
import org.openjdk.jcstress.vm.AllocProfileSupport;
import org.openjdk.jcstress.infra.runners.FootprintEstimator;
import org.openjdk.jcstress.infra.runners.VoidThread;
import org.openjdk.jcstress.infra.runners.LongThread;
import org.openjdk.jcstress.infra.runners.CounterThread;
import com.astocoding.unsafe.ModifyFields.VisibilityTest;
import org.openjdk.jcstress.infra.results.I_Result;

public final class ModifyFields_VisibilityTest_jcstress extends Runner<I_Result> {

    volatile WorkerSync workerSync;
    VisibilityTest[] gs;
    I_Result[] gr;

    public ModifyFields_VisibilityTest_jcstress(ForkedTestConfig config) {
        super(config);
    }

    @Override
    public void sanityCheck(Counter<I_Result> counter) throws Throwable {
        sanityCheck_API(counter);
        sanityCheck_Footprints(counter);
    }

    private void sanityCheck_API(Counter<I_Result> counter) throws Throwable {
        final VisibilityTest s = new VisibilityTest();
        final I_Result r = new I_Result();
        VoidThread a0 = new VoidThread() { protected void internalRun() {
            s.method(r);
        }};
        VoidThread a1 = new VoidThread() { protected void internalRun() {
            s.testWriteVisibility();
        }};
        a0.start();
        a1.start();
        a0.join();
        if (a0.throwable() != null) {
            throw a0.throwable();
        }
        a1.join();
        if (a1.throwable() != null) {
            throw a1.throwable();
        }
        counter.record(r);
    }

    private void sanityCheck_Footprints(Counter<I_Result> counter) throws Throwable {
        config.adjustStrideCount(new FootprintEstimator() {
          public void runWith(int size, long[] cnts) {
            long time1 = System.nanoTime();
            long alloc1 = AllocProfileSupport.getAllocatedBytes();
            VisibilityTest[] ls = new VisibilityTest[size];
            I_Result[] lr = new I_Result[size];
            for (int c = 0; c < size; c++) {
                VisibilityTest s = new VisibilityTest();
                I_Result r = new I_Result();
                lr[c] = r;
                ls[c] = s;
            }
            LongThread a0 = new LongThread() { public long internalRun() {
                long a1 = AllocProfileSupport.getAllocatedBytes();
                for (int c = 0; c < size; c++) {
                    ls[c].method(lr[c]);
                }
                long a2 = AllocProfileSupport.getAllocatedBytes();
                return a2 - a1;
            }};
            LongThread a1 = new LongThread() { public long internalRun() {
                long a1 = AllocProfileSupport.getAllocatedBytes();
                for (int c = 0; c < size; c++) {
                    ls[c].testWriteVisibility();
                }
                long a2 = AllocProfileSupport.getAllocatedBytes();
                return a2 - a1;
            }};
            a0.start();
            a1.start();
            try {
                a0.join();
                cnts[0] += a0.result();
            } catch (InterruptedException e) {
            }
            try {
                a1.join();
                cnts[0] += a1.result();
            } catch (InterruptedException e) {
            }
            for (int c = 0; c < size; c++) {
                counter.record(lr[c]);
            }
            long time2 = System.nanoTime();
            long alloc2 = AllocProfileSupport.getAllocatedBytes();
            cnts[0] += alloc2 - alloc1;
            cnts[1] += time2 - time1;
        }});
    }

    @Override
    public ArrayList<CounterThread<I_Result>> internalRun() {
        int len = config.strideSize * config.strideCount;
        gs = new VisibilityTest[len];
        gr = new I_Result[len];
        for (int c = 0; c < len; c++) {
            gs[c] = new VisibilityTest();
            gr[c] = new I_Result();
        }
        workerSync = new WorkerSync(false, 2, config.spinLoopStyle);

        control.isStopped = false;

        if (config.localAffinity) {
            try {
                AffinitySupport.tryBind();
            } catch (Exception e) {
                // Do not care
            }
        }

        ArrayList<CounterThread<I_Result>> threads = new ArrayList<>(2);
        threads.add(new CounterThread<I_Result>() { public Counter<I_Result> internalRun() {
            return task_method();
        }});
        threads.add(new CounterThread<I_Result>() { public Counter<I_Result> internalRun() {
            return task_testWriteVisibility();
        }});

        for (CounterThread<I_Result> t : threads) {
            t.start();
        }

        if (config.time > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(config.time);
            } catch (InterruptedException e) {
            }
        }

        control.isStopped = true;

        return threads;
    }

    private void jcstress_consume(Counter<I_Result> cnt, int a) {
        VisibilityTest[] ls = gs;
        I_Result[] lr = gr;
        int len = config.strideSize * config.strideCount;
        int left = a * len / 2;
        int right = (a + 1) * len / 2;
        for (int c = left; c < right; c++) {
            I_Result r = lr[c];
            VisibilityTest s = ls[c];
            ls[c] = new VisibilityTest();
            cnt.record(r);
            r.r1 = 0;
        }
    }

    private void jcstress_sink(int v) {};
    private void jcstress_sink(short v) {};
    private void jcstress_sink(byte v) {};
    private void jcstress_sink(char v) {};
    private void jcstress_sink(long v) {};
    private void jcstress_sink(float v) {};
    private void jcstress_sink(double v) {};
    private void jcstress_sink(Object v) {};

    private Counter<I_Result> task_method() {
        int len = config.strideSize * config.strideCount;
        int stride = config.strideSize;
        Counter<I_Result> counter = new Counter<>();
        if (config.localAffinity) AffinitySupport.bind(config.localAffinityMap[0]);
        while (true) {
            WorkerSync sync = workerSync;
            if (sync.stopped) {
                return counter;
            }
            int check = 0;
            for (int start = 0; start < len; start += stride) {
                run_method(gs, gr, start, start + stride);
                check += 2;
                sync.awaitCheckpoint(check);
            }
            jcstress_consume(counter, 0);
            if (sync.tryStartUpdate()) {
                workerSync = new WorkerSync(control.isStopped, 2, config.spinLoopStyle);
            }
            sync.postUpdate();
        }
    }

    private void run_method(VisibilityTest[] gs, I_Result[] gr, int start, int end) {
        VisibilityTest[] ls = gs;
        I_Result[] lr = gr;
        for (int c = start; c < end; c++) {
            VisibilityTest s = ls[c];
            I_Result r = lr[c];
            jcstress_sink(r.jcstress_trap);
            s.method(r);
        }
    }

    private Counter<I_Result> task_testWriteVisibility() {
        int len = config.strideSize * config.strideCount;
        int stride = config.strideSize;
        Counter<I_Result> counter = new Counter<>();
        if (config.localAffinity) AffinitySupport.bind(config.localAffinityMap[1]);
        while (true) {
            WorkerSync sync = workerSync;
            if (sync.stopped) {
                return counter;
            }
            int check = 0;
            for (int start = 0; start < len; start += stride) {
                run_testWriteVisibility(gs, gr, start, start + stride);
                check += 2;
                sync.awaitCheckpoint(check);
            }
            jcstress_consume(counter, 1);
            if (sync.tryStartUpdate()) {
                workerSync = new WorkerSync(control.isStopped, 2, config.spinLoopStyle);
            }
            sync.postUpdate();
        }
    }

    private void run_testWriteVisibility(VisibilityTest[] gs, I_Result[] gr, int start, int end) {
        VisibilityTest[] ls = gs;
        I_Result[] lr = gr;
        for (int c = start; c < end; c++) {
            VisibilityTest s = ls[c];
            s.testWriteVisibility();
        }
    }

}

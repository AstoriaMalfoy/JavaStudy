package org.example;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/6/7 17:36
 */
public class Executor implements
        Watcher,                                /* 是一个事件监听器 */
        Runnable,
        DataMonitor.DataMonitorListener         /* 是一个数据监听器 */
{
    @Override
    public void run() {

    }

    @Override
    public void process(WatchedEvent event) {

    }

    @Override
    public void exists(byte[] data) {

    }

    @Override
    public void closing(int rc) {

    }
}

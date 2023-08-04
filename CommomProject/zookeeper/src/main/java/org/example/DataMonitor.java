package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by IntelliJ LITAO.
 * <p>
 * 一个完整的zookeeper节点监控应该包含以下部分
 * 1. 针对于当前节点是否存在的监控
 * 2. 针对于当前节值变化的监控
 * 3. 站对于当前节点孩子节点的增减的变化
 *
 * @author litao
 * @since 2023/6/7 16:46
 */
@Slf4j
@Data
@NoArgsConstructor
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {
    /**
     * zookeeper客户端
     */
    private ZooKeeper zooKeeper;
    /**
     * 监听的znode节点
     */
    private String zNode;
    /**
     * 子节点的监听器
     */
    private Watcher childWatcher;
    /**
     * 数据监听器
     */
    private DataMonitorListener dataMonitorListener;


    public DataMonitor(
            ZooKeeper zk,
            String zNode,
            Watcher childWatcher,
            DataMonitorListener listener
    ) {
        this.zooKeeper = zk;
        this.zNode = zNode;
        this.childWatcher = childWatcher;
        this.dataMonitorListener = listener;
        // 针对于当前节点是否存在的Watch
        zk.exists(this.zNode,true, this, null);
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        log.info("DataMonitor processResult method invoke, i: {}, s: {}, o: {}, stat: {}", i, s, o, stat);
    }

    /**
     * 该接口来自 @Watch
     * 该接口指定了事件的处理方式，当事件发生的时候，会调用该接口的process方法
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info("DataMonitor process method invoke, watchedEvent: {}", watchedEvent);
    }


    public interface DataMonitorListener {
        void exists(byte[] data);

        void closing(int rc);
    }

}

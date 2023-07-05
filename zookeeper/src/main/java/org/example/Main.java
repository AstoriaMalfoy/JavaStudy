package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/6/6 17:51
 */
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        ZooKeeper zooKeeper = main.connectZooKeeperClient();
        if (zooKeeper != null){
            log.info("zooKeeper create success state: {}", zooKeeper.getState());
        }
        main.createDataMonitor(zooKeeper, "/test");
        while (true){
            Thread.sleep(1000);
        }
    }

    public ZooKeeper connectZooKeeperClient(){
        ZooKeeper zooKeeper = null;
        try {
            // 创建zookeeper客户端，创建连接可以提供多个IP地址，需要多个IP地址之间使用,分隔，第二个参数是会话超时时间，单位毫秒
            // 最后提供一个watchEvent对象，当zookeeper连接状态发生变化的时候，会调用该监听事件的process方法
            zooKeeper = new ZooKeeper("localhost:2181", 3000, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                    log.info("zookeeper连接成功。。。。。。 连接状态 。。。。。。同步连接成功");
                }
                else if (watchedEvent.getState() == Watcher.Event.KeeperState.Disconnected){
                    log.info("zookeeper连接断开。。。。。。 连接状态 。。。。。。断开连接");
                }else{
                    log.info("zookeeper连接其他状态。。。。。。 连接状态 。。。。。。其他状态");
                }

            });
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return zooKeeper;
    }

    public DataMonitor createDataMonitor(ZooKeeper zooKeeper, String zNode){
        return new DataMonitor(zooKeeper, zNode, null, null);
    }

    

}
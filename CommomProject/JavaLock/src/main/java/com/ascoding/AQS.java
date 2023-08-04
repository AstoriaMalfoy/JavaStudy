package com.ascoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/24 9:54
 */
public class AQS{


    // AQS是一个抽象类，看起来貌似所有的函数都有实现，但是实际上都是空实现，默认方法都是抛出异常，这是因为AQS是一个模板类，它的子类需要实现它的抽象方法，来完成同步状态的管理。
    // AQS中需要实现的方法主要有以下几个：
    // 1. isHeldExclusively()：是否处于占用状态。只有用到condition才需要去实现它。
    // 2. tryAcquire(int)：独占方式。尝试获取资源，成功则返回true，失败则返回false。
    // 3. tryRelease(int)：独占方式。尝试释放资源，成功则返回true，失败则返回false。
    // 4. tryAcquireShared(int)：共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
    // 5. tryReleaseShared(int)：共享方式。尝试释放资源，如果释放后允许唤醒后续等待节点返回true，否则返回false。
    // 6. 以上方法都是默认抛出UnsupportedOperationException异常，需要子类自己实现。


    // AQS Node
    // AQS中的Node是一个双向链表，每个Node代表一个线程，每个Node中包含了线程的状态，以及前驱和后继节点的引用。
    // AQS中的Node有两种类型，一种是共享节点，一种是独占节点。共享节点用于实现共享锁，独占节点用于实现独占锁。
    // 共享节点的waitStatus有以下几种：
    // 1. CANCELLED：表示当前节点已取消。
    // 2. SIGNAL：表示后继节点的线程需要运行（共享模式）。
    // 3. CONDITION：表示当前节点在condition队列中。
    // 4. PROPAGATE：表示下一次共享模式的acquireShared能够得到执行。
    // 5. 0：表示当前节点在sync队列中，等待获取锁。
    // 独占节点的waitStatus有以下几种：
    // 1. CANCELLED：表示当前节点已取消。
    // 2. SIGNAL：表示后继节点的线程需要运行（独占模式）。
    // 3. CONDITION：表示当前节点在condition队列中。
    // 0：表示当前节点在sync队列中，等待获取锁。
    // 共享节点和独占节点的区别在于，共享节点的waitStatus中有PROPAGATE状态，而独占节点没有。
    // 共享节点的PROPAGATE状态表示下一次共享模式的acquireShared能够得到执行，而独占节点没有这个状态。
    // 共享节点的PROPAGATE状态是在共享模式下，当一个节点释放锁后，会唤醒后继节点，如果后继节点还是共享节点，那么会继续唤醒后继节点，直到遇到独占节点为止。

    // AQS 中断检测
    // AQS中断检测是通过调用Thread.interrupted()方法来检测的，该方法会清除当前线程的中断标记。
    // 如果需要保留中断标记，可以使用Thread.currentThread().isInterrupted()方法来检测。
    // AQS中断检测的逻辑是，如果当前线程已经被中断，则抛出异常，否则返回false。



    // AQS 源码
    // private Node enq(final Node node) ;
    // 在连表尾部添加一个节点,如果当前队列为空,则将头节点指向该节点,否则将尾节点的next指向该节点,然后将尾节点指向该节点。
    // private Node addWaiter(Node mode) ;
    // 创建一个新的节点,并将其加入到队列尾部,如果当前线程已经被中断,则抛出异常,如果当前队列为空,则将头节点指向该节点,否则将尾节点的next指向该节点,然后将尾节点指向该节点。
    // 该方法通过CAS的方式将节点插入到队列，如果插入失败，则会通过enq方法将节点插入到队列尾部。


    // AQS 中的源码是无法单独满足锁的需求的，实际上在JUC包中，需要使用AQS的类也是自己实现了一个同步器，然后通过对同步器的操作来是实现并发控制
    // 以ReentrantLock为例，ReentrantLock中的Sync类继承了AQS,而又有FairSync和NonfairSync两个子类继承了Sync类，分别实现了公平锁和非公平锁。
    // 这时，只有FairSync和NonfairSync两个类才是真正实现了锁的类，能够实现完整的并发访问控制，所以建议在阅读源码的时候，优先从这两个类开始阅读。


    // AQS是JUC包的基础，其中的功能不仅仅是为了简单的实现某个互斥锁等功能，JUC中复杂的并发控制，每个功能只用到了AQS中的一小部分功能。





    public static void main(String[] args) {
        AbstractQueuedSynchronizer aqs = new AbstractQueuedSynchronizer() {

        };

        Integer waitTime = 1000;
        // aql export method


        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                aqs.acquire(waitTime);
                System.out.println("thread already get lock");
                // sleep 2s
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread release lock");
                aqs.release(waitTime);
            });
        }
    }

}

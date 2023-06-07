package com.astocoding;

import com.google.common.cache.*;

import javax.swing.plaf.TableHeaderUI;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/13 17:00
 */
public class GuavaCache {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 通过builder模式构建缓存
        LoadingCache<String,Object> cache = CacheBuilder.newBuilder()
                // 设置最大长度
                .maximumSize(3)
                // 设置过期时间
                .expireAfterWrite(3, TimeUnit.SECONDS)

                .recordStats().removalListener(
                        new RemovalListener<Object, Object>() {
                            @Override
                            public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                                System.out.println(removalNotification.getKey() + ":" + removalNotification.getCause());
                            }
                        }
                ).build(
                        // 在创建的时候，通过CacheLoader来获取数据源
                        new CacheLoader<String, Object>() {
                            @Override
                            public Object load(String s) throws Exception {
                                System.out.println("get cache data from cacheLoader function");
                                return "value:" + s;
                            }
                        }
                );
        initCache(cache);
        System.out.println(cache.size());
        desplayCache(cache);
        System.out.println("=============================");

        Thread.sleep(1000);
        System.out.println(cache.getIfPresent("1"));;
        Thread.sleep(2500);
        System.out.println("=============================");

        Thread.sleep(2500);
        System.out.println("=============================");
        System.out.println(get("123",cache));

    }

    public static Object get(String key,LoadingCache cache) throws ExecutionException {
        return cache.get(key, () -> {
            String rollbackValue = "value:" + key;
            System.out.println("get data from rollback");
            cache.put(key,rollbackValue);
            return rollbackValue;
        });
    }


    public static void initCache(LoadingCache cache) throws ExecutionException {
        for (int i=0;i<3;i++){
           cache.get(String.valueOf(i));
        }
    }

    public static void desplayCache(LoadingCache cache){
        Iterator iterator = cache.asMap().entrySet().iterator();
        while (iterator.hasNext()){
            System.out.println(String.valueOf(iterator.next()));
        }
    }

}

package com.yihua.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 安全线程池
 *
 * @author wangxusheng
 * @date 2023/12/19 10:51
 * @change 2023/12/19 10:51 by wangxusheng for init
 */
@Component
public class SafeThreadPool {

    @Bean
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10
                ,10,0,TimeUnit.MINUTES,new ArrayBlockingQueue<>(15)
                ,new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }
}

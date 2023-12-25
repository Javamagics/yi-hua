package com.yihua.common;


/**
 * 处理当前线程的方法
 *
 * @author wangxusheng
 * @date 2023/11/24 15:25
 * @change 2023/11/24 15:25 by wangxusheng for init
 */
public class CurrentThreadMethods {

    /**
     * 获取当前线程id
     * @return
     */
    public static String getCurrentThreadId() {
        return String.valueOf(Thread.currentThread().getId());
    }

}

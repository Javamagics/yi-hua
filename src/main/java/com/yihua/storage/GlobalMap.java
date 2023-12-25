package com.yihua.storage;

import com.yihua.entity.SqlEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 【单例】全局Map
 *
 * @author wangxusheng
 * @date 2023/11/27 19:21
 * @change 2023/11/27 19:21 by wangxusheng for init
 */
public class GlobalMap {

    private static Map<String, SqlEntity> sqlMap = new ConcurrentHashMap<>(16);

    public static void set(String key, SqlEntity value) {
        sqlMap.put(key, value);
    }

    public static SqlEntity get(String key) {
        return sqlMap.get(key);
    }

    public static void remove(String key) {
        sqlMap.remove(key);
    }

}

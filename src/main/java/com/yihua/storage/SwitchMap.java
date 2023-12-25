package com.yihua.storage;

import java.util.HashMap;
import java.util.Map;

/**
 * 控制是否拦截sql
 *
 * @author wangxusheng
 * @date 2023/12/5 15:46
 * @change 2023/12/5 15:46 by wangxusheng for init
 */
public class SwitchMap {

    private static Map<String, Boolean> switchMap = new HashMap<>(16);

    public static void set(String key, Boolean value) {
        switchMap.put(key, value);
    }

    public static Boolean get(String key) {
        return switchMap.get(key);
    }

}

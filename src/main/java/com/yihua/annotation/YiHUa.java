package com.yihua.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取接口运行时间及Error错误注解
 *
 * @author wangxusheng
 * @date 2023/10/25 17:51
 * @change 2023/10/25 17:51 by wangxusheng for init
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YiHUa {

    /**
     * 是否开启测试开关 默认开启
     * @return
     */
    boolean button() default true;
}

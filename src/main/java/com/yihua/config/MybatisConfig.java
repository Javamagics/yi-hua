package com.yihua.config;

import com.yihua.interceptor.MyBatisSqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangxusheng
 * @date 2023/11/28 10:14
 * @change 2023/11/28 10:14 by wangxusheng for init
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MyBatisSqlInterceptor myBatisSqlInterceptor() {
        return new MyBatisSqlInterceptor();
    }
}

package com.yihua.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.yihua.banner.PrintBanner;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangxusheng
 * @date 2023/12/20 14:38
 * @change 2023/12/20 14:38 by wangxusheng for init
 */
@EnableConfigurationProperties(ApiTestLogProperties.class)
@AutoConfiguration
@Component
public class ApiTestConfig implements CommandLineRunner {

    private static final Logger log = (Logger) LoggerFactory.getLogger(ApiTestLogProperties.class);

    private final ApiTestLogProperties apiTestLogProperties;

    private final ApiTestSwitchProperties apiTestSwitchProperties;

    @Autowired
    public ApiTestConfig(ApiTestLogProperties apiTestLogProperties, ApiTestSwitchProperties apiTestSwitchProperties) {
        this.apiTestLogProperties = apiTestLogProperties;
        this.apiTestSwitchProperties = apiTestSwitchProperties;
    }


    @Override
    public void run(String... args)  {
        //设置日志
        LogConfig logConfig = new LogConfig(apiTestLogProperties);
//        logConfig.configure();
        String path = apiTestLogProperties.getPath();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.putObject("logDirectory",path);
        loggerContext.putProperty("logDirectory",path);

        //打印启动标识
        System.out.println(PrintBanner.print());
    }
}

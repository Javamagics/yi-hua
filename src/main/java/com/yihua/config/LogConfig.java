package com.yihua.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ch.qos.logback.classic.Level;
import ch.qos.logback.core.ConsoleAppender;

import java.io.File;


/**
 * 配置日志
 *
 * @author wangxusheng
 * @date 2023/12/21 15:02
 * @change 2023/12/21 15:02 by wangxusheng for init
 */
@Component
public class LogConfig {

    private final ApiTestLogProperties apiTestLogProperties;

    @Autowired
    public LogConfig(ApiTestLogProperties apiTestLogProperties) {
        this.apiTestLogProperties = apiTestLogProperties;
    }


    public  void configure() {
        if (StringUtils.isNotEmpty(apiTestLogProperties.getPath())) {
            //配置了日志输出路径 输出日志到文件中
            toFileConfig(apiTestLogProperties.getPath());
        } else {
            toConsoleConfig();
        }

    }

    /**
     * toConsoleConfig
     * 输出日志到控制台
     *
     * @return void
     * @author wangxusheng
     * @date 2023/12/21 15:05:44
     * @change 2023/12/21 15:05:44 by wangxusheng for init
     * @since 1.0.0
     */
    private void toConsoleConfig() {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%msg%n");
        encoder.setContext(rootLogger.getLoggerContext());
        encoder.start();

        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setName("STDOUT");
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        rootLogger.addAppender(consoleAppender);
    }

    /**
     * toFileConfig
     * 输出日志到文件
     *
     * @return void
     * @author wangxusheng
     * @date 2023/12/21 15:07:00
     * @change 2023/12/21 15:07:00 by wangxusheng for init
     * @since 1.0.0
     */
    private void toFileConfig(String path) {
        // 获取根日志记录器
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%msg%n");
        encoder.setContext(rootLogger.getLoggerContext());
        encoder.start();
        // 定义日志文件路径和名称
        String logFilePath = path;
        File logFile = new File(logFilePath);

        // 创建并配置FileAppender
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(rootLogger.getLoggerContext());
        fileAppender.setContext(rootLogger.getLoggerContext());
        fileAppender.setFile(logFile.getAbsolutePath());
        fileAppender.start();

        // 将FileAppender添加到根日志记录器
        rootLogger.addAppender(fileAppender);
    }

}

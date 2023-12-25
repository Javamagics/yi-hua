package com.yihua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangxusheng
 * @date 2023/12/20 14:38
 * @change 2023/12/20 14:38 by wangxusheng for init
 */
@Data
@Component
@ConfigurationProperties("yi-hua.log")
public class ApiTestLogProperties {

    /**
     * 日志输出路径（如果不指定，则默认与工程日志合并）
     */
    private String path;

}

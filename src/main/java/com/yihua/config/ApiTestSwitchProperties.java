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
@ConfigurationProperties("yi-hua.switch")
public class ApiTestSwitchProperties {

    /**
     * 全局开关-是否开启注解
     */
    private Boolean globalSwitch = Boolean.TRUE;


}

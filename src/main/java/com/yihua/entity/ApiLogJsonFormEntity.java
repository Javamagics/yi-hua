package com.yihua.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口日志Json格式实体
 *
 * @author wangxusheng
 * @date 2023/12/21 10:47
 * @change 2023/12/21 10:47 by wangxusheng for init
 */
@Data
public class ApiLogJsonFormEntity implements Serializable {

    private static final long serialVersionUID = 4726678006952054008L;

    /**
     * 线程id
     */
    @JSONField(name = "线程id")
    private String currentThreadId;

    /**
     * 接口名称
     */
    @JSONField(name = "接口名称")
    private String apiName;

    /**
     * 描述
     */
    @JSONField(name = "接口名称")
    private String description;

    /**
     * 接口耗时
     */
    @JSONField(name = "接口名称")
    private String apiDuration;

    /**
     * java代码逻辑耗时
     */
    @JSONField(name = "接口名称")
    private String logicDuration = "";


    /**
     * 接口io次数
     */
    @JSONField(name = "接口名称")
    private Integer ioCount = 0;

    /**
     * 接口运行时的sql情况
     */
    @JSONField(name = "接口名称")
    private SqlEntity sqlEntity;




}

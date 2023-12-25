package com.yihua.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口日志实体类
 *
 * @author wangxusheng
 * @date 2023/12/19 10:24
 * @change 2023/12/19 10:24 by wangxusheng for init
 */
@Data
public class ApiLogEntity implements Serializable {

    private static final long serialVersionUID = 4746678006952054008L;

    /**
     * 线程id
     */
    private String currentThreadId;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 描述
     */
    private String description="";

    /**
     * 接口耗时
     */
    private String apiDuration;

    /**
     * java代码逻辑耗时
     */
    private String logicDuration = "";


    /**
     * 接口io次数
     */
    private Integer ioCount = 0;

    /**
     * 接口运行时的sql情况
     */
    private SqlEntity sqlEntity = new SqlEntity();


    public String toStringNoSql() {
        return "接口运行日志:" +
                "当前线程id='" + currentThreadId + '\'' +
                ", 接口名称='" + apiName + '\'' +
                ", 描述='" + description + '\'' +
                ", 接口总运行时间='" + apiDuration + '\'' +
                ", 接口逻辑运行时间='" + logicDuration + '\'' +
                ", 与数据库交互次数=" + ioCount;

    }
}

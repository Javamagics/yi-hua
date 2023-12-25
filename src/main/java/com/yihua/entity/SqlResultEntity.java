package com.yihua.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 记录具体的sql语句和结果所占RamSize
 *
 * @author wangxusheng
 * @date 2023/11/30 16:58
 * @change 2023/11/30 16:58 by wangxusheng for init
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SqlResultEntity implements Serializable {

    private static final long serialVersionUID = -5978331019434657753L;

    /**
     * sql语句
     */
    private String sqlStr;


    /**
     * 当前线程执行sql语句返回结果 所占内存的实际大小(Bytes)
     */
    private Long resultRamSize = 0L;

    /**
     * 当前sql的执行时间
     */
    private Long wasteTime;

    /**
     * 当前线程与当前程序最大内存所占比值
     */
    private String ramRate;

    /**
     * sql查询结果是否为大对象
     */
    private Boolean isBig;

    /**
     * sql运行前Eden区空闲内存
     */
    private Long edenFreeSpace;

    /**
     * sql运行前Survivor区空闲内存
     */
    private Long survivorFreeSpace;

    @Override
    public String toString() {
        return  "sql语句 = "+sqlStr + '\'' +
                ", 所占内存大小=" + resultRamSize +
                ", 耗费时间=" + wasteTime +
                ", 与堆内存比值='" + ramRate + '\'' +
                ", 是否大对象=" + isBig ;
    }
}

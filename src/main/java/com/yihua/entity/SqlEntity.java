package com.yihua.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * sql实体类
 *
 * @author wangxusheng
 * @date 2023/11/27 19:22
 * @change 2023/11/27 19:22 by wangxusheng for init
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlEntity implements Serializable {
    private static final long serialVersionUID = -5415486310737201255L;
    /**
     * 执行当前sql的线程id
     */
    private String executeSqlThreadId;

    /**
     * 当前sql对应结果集合
     */
    private List<SqlResultEntity> sqlResultEntityList;
}

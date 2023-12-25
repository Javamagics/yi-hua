package com.yihua.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * sql执行计划实体
 *
 * @author wangxusheng
 * @date 2023/12/5 10:26
 * @change 2023/12/5 10:26 by wangxusheng for init
 */
@Data
public class SqlExplainEntity implements Serializable {

    private static final long serialVersionUID = 5233244602137020977L;

    private Integer id;

    private String selectType;

    private String table;

    private String partitions;

    private String type;

    private String possibleKeys;

    private String key;

    private String keyLen;

    private String ref;

    private Integer rows;

    private String filtered;

    private String extra;


}

package com.yihua.common;

import ch.qos.logback.classic.Logger;
import com.yihua.entity.ApiLogEntity;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * 日志
 *
 * @author wangxusheng
 * @date 2023/12/19 14:49
 * @change 2023/12/19 14:49 by wangxusheng for init
 */
public class LogMethods {
    private static final Logger log = (Logger) LoggerFactory.getLogger(LogMethods.class);
    /**
     * 写日志
     *
     * @param apiLogEntity
     */
    public static void writeApiLog(ApiLogEntity apiLogEntity) {
        log.info("-----------------------------------------------------{} 接口运行情况----------------------------------------------------------------------------------------------------",apiLogEntity.getApiName());
        log.info(apiLogEntity.toStringNoSql());
        log.info("-----------------------------------------------------sql 运行情况----------------------------------------------------------------------------------------------------");
        if (!ObjectUtils.isEmpty(apiLogEntity.getSqlEntity()) && !CollectionUtils.isEmpty(apiLogEntity.getSqlEntity().getSqlResultEntityList())) {
            int i = 0;
            apiLogEntity.getSqlEntity().getSqlResultEntityList()
                    .forEach(x ->{log.info(x.toString());});
        }
        log.info("-----------------------------------------------------{} api log end------------------------------------------------------------------------------------------------------",apiLogEntity.getApiName());
        log.info("\n");
        log.info("\n");
        log.info("\n");
        log.info("\n");

    }

    public static void writeApiLogByThreadId(String threadId) {

    }

    /**
     * getLogTableTemplate
     * TODO 将接口运行记录 打印成一种Table
     * @param apiLogEntity
     * @return  java.lang.String
     * @since 1.0.0
     * @author wangxusheng
     * @date 2023/12/21 10:43:00
     * @change
     * 2023/12/21 10:43:00 by wangxusheng for init
     */
    private String getLogTableTemplate(ApiLogEntity apiLogEntity) {
        //获取表格总行数
        int sqlRow = apiLogEntity.getSqlEntity().getSqlResultEntityList().size();
        int totalRow = sqlRow + 3;
        //设置表格每列宽度
        int columnSize = apiLogEntity.getApiName().length() + 20;
        String emptyString = getDesignationLengthEmptyString(columnSize);
        System.out.println(apiLogEntity);
        String[][] template = new String[sqlRow][7];
        template[0][0] = "接口名称"+emptyString;
        template[0][1] = "|";
        template[0][2] = "总耗时"+emptyString;
        template[0][3] = "|";
        template[0][4] = "逻辑耗时"+emptyString;
        template[0][5] = "|";
        template[0][6] = "数据库交互次数"+emptyString;

        template[1][0] = apiLogEntity.getApiName()+getDesignationLengthEmptyString(columnSize-apiLogEntity.getApiName().length());
        template[1][1] = "|";
        template[1][2] = apiLogEntity.getApiDuration()+getDesignationLengthEmptyString(columnSize-apiLogEntity.getApiName().length());;
        template[1][3] = "|";
        template[1][4] = apiLogEntity.getLogicDuration()+getDesignationLengthEmptyString(columnSize-apiLogEntity.getApiName().length());;
        template[1][5] = "|";
        template[1][6] = String.valueOf(apiLogEntity.getIoCount()+getDesignationLengthEmptyString(columnSize-apiLogEntity.getApiName().length()));
        return "";
    }

    /**
     * 获取一个长度为10的空字符串
     * @return
     */
    private static String getTenLengthEmptyString() {
        return "          ";
    }

    private static String getDesignationLengthEmptyString(Integer length) {
        if (ObjectUtils.isEmpty(length)) {
            return "";
        }
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            result.append(" ");
        }
        return result.toString();
    }


}

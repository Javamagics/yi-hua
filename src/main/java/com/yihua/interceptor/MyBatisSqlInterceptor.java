package com.yihua.interceptor;

import com.yihua.common.CurrentThreadMethods;
import com.yihua.common.MemoryMethods;
import com.yihua.entity.SqlEntity;
import com.yihua.entity.SqlResultEntity;
import com.yihua.storage.GlobalMap;
import com.yihua.storage.SwitchMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * 获取mybatis中的sql语句
 *
 * @author wangxusheng
 * @date 2023/11/24 15:15
 * @change 2023/11/24 15:15 by wangxusheng for init
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyBatisSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //执行sql
        long sqlStart = System.currentTimeMillis();
        //获取执行sql前的Eden区空闲空间大小
        Long currentEdenSpaceFreeMemory = MemoryMethods.getCurrentEdenSpaceFreeMemory();
        //获取执行sql前的Survivor区空闲空间大小
        Long currentSurvivorSpaceFreeMemory = MemoryMethods.getCurrentSurvivorSpaceFreeMemory();
        Object result = invocation.proceed();
        long sqlWasteTime = System.currentTimeMillis() - sqlStart;
        //获取当前执行的sql语句
        Executor executor = (Executor) invocation.getTarget();
        String sql = getSql(invocation);
        System.out.println(sql);
        //获取当前的线程id
        String currentThreadId = CurrentThreadMethods.getCurrentThreadId();
        //构建sqlResult
        SqlResultEntity sqlResult = new SqlResultEntity();
        sqlResult.setSqlStr(sql);
        sqlResult.setWasteTime(sqlWasteTime);
        sqlResult.setResultRamSize(MemoryMethods.getObjectSize(result));
        sqlResult.setEdenFreeSpace(currentEdenSpaceFreeMemory);
        sqlResult.setSurvivorFreeSpace(currentSurvivorSpaceFreeMemory);
        //将线程id和sql存储到GlobalMap中
        //1.先判断是否已经有其他sql语句
        SqlEntity sqlEntity = GlobalMap.get(currentThreadId);
        if (ObjectUtils.isEmpty(sqlEntity)) {
            //没有
            List<SqlResultEntity> sqlResultEntityList = new ArrayList<>();
            sqlResultEntityList.add(sqlResult);
            sqlEntity = new SqlEntity(currentThreadId, sqlResultEntityList);
        } else {
            //有
            List<SqlResultEntity> sqlResultEntityList = sqlEntity.getSqlResultEntityList();
            sqlResultEntityList.add(sqlResult);
            sqlEntity.setSqlResultEntityList(sqlResultEntityList);
        }
        GlobalMap.set(currentThreadId, sqlEntity);
        return result;
    }

    @Override
    public Object plugin(Object target) {
        //当开关为开启状态才能加载自定义插件
        if (SwitchMap.get(CurrentThreadMethods.getCurrentThreadId())
                && target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * getSql
     * 获取最终sql
     *
     * @param invocation
     * @return java.lang.String
     * @author wangxusheng
     * @date 2023/12/05 16:22:59
     * @change 2023/12/05 16:22:59 by wangxusheng for init
     * @since 1.0.0
     */
    public static String getSql(Invocation invocation) {
        // 获取参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = null;
        if (!ObjectUtils.isEmpty(invocation.getArgs()) &&
                invocation.getArgs().length > 1) {
            parameter = args[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        Configuration configuration = mappedStatement.getConfiguration();
        if (parameterMappings != null && parameterMappings.size() != 0 && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * getParameterValue
     * 参数转换
     *
     * @param obj
     * @return java.lang.String
     * @author wangxusheng
     * @date 2023/12/05 15:43:21
     * @change 2023/12/05 15:43:21 by wangxusheng for init
     * @since 1.0.0
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }


}

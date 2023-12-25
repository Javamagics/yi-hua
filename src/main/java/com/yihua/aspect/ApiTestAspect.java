package com.yihua.aspect;


import com.yihua.annotation.YiHUa;
import com.yihua.common.CurrentThreadMethods;
import com.yihua.common.LogMethods;
import com.yihua.common.MemoryMethods;
import com.yihua.config.ApiTestSwitchProperties;
import com.yihua.entity.ApiLogEntity;
import com.yihua.entity.SqlEntity;
import com.yihua.pool.SafeThreadPool;
import com.yihua.storage.GlobalMap;
import com.yihua.storage.SwitchMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 获取接口运行时长及Error切面类
 *
 * @author wangxusheng
 * @date 2023/10/25 17:33
 * @change 2023/10/25 17:33 by wangxusheng for init
 */
@Component
@Aspect
public class ApiTestAspect {

    private static final Logger log = LoggerFactory.getLogger(LogMethods.class);

    @Pointcut("@annotation(com.yihua.annotation.YiHUa)")
    private void pointCutMethod() {

    }

    @Resource
    private ApiTestSwitchProperties apiTestSwitchProperties;

    /**
     * methodExporter
     * 计算接口运行时间、内存使用情况、以及sql优化建议
     *
     * @param joinPoint
     * @return java.lang.Object
     * @author wangxusheng
     * @date 2023/10/27 15:14:13
     * @change 2023/10/27 15:14:13 by wangxusheng for init
     * @since 1.0.0
     */
    @Around("pointCutMethod()")
    public Object methodExporter(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取方法
        MethodSignature targetMethodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的ApiTest注解
        YiHUa apiTestAnnotation = targetMethodSignature.getMethod().getAnnotation(YiHUa.class);
        //执行该方法
        Object proceed = null;
        try {
            if (!apiTestSwitchProperties.getGlobalSwitch()||apiTestAnnotation == null || !apiTestAnnotation.button()) {
                //api注解为空 或者 关闭了该注解的功能
                SwitchMap.set(CurrentThreadMethods.getCurrentThreadId(), Boolean.FALSE);
                return joinPoint.proceed();
            }
            //设置全局开关
            SwitchMap.set(CurrentThreadMethods.getCurrentThreadId(), Boolean.TRUE);
            proceed = joinPoint.proceed();
        } catch (OutOfMemoryError e) {
            //如果已经发生了内存溢出，直接记录日志
            log.info("------------------------message:{} Out Of Memory-----------------------------------",targetMethodSignature.getName());
            throw e;
        }
        //获取当前线程id
        String currentThreadId = CurrentThreadMethods.getCurrentThreadId();
        long endTime = System.currentTimeMillis();
        //接口总运行时间
        long durationTime = endTime - startTime;
        //异步线程去记录日志
        SafeThreadPool.getThreadPoolExecutor()
                .submit(() -> LogMethods.writeApiLog(prepareLogEntity(currentThreadId, durationTime, targetMethodSignature.getName())));
        return proceed;
    }


    /**
     * 准备 日志记录 需要的数据
     *
     * @param threadId
     * @param
     * @return
     */
    private ApiLogEntity prepareLogEntity(String threadId, Long apiDurationTime, String methodName) {
        ApiLogEntity result = new ApiLogEntity();
        //在GlobalMap中查找当前线程执行的sql语句
        SqlEntity sqlEntity = GlobalMap.get(threadId);
        //在map中移除结果集
        GlobalMap.remove(threadId);
        if (ObjectUtils.isEmpty(sqlEntity)) {
            return result;
        }
        //获取内存相关参数
        MemoryMethods.getSqlResultRamUseRate(sqlEntity);
        //构建日志实体
        result.setApiName(methodName);
        result.setApiDuration(String.valueOf(apiDurationTime));
        result.setSqlEntity(sqlEntity);
        if (!ObjectUtils.isEmpty(sqlEntity) && !CollectionUtils.isEmpty(sqlEntity.getSqlResultEntityList())) {
            result.setIoCount(sqlEntity.getSqlResultEntityList().size());
            //获取sql总耗时
            AtomicReference<Long> sqlDuration = new AtomicReference<>(0L);
            sqlEntity.getSqlResultEntityList()
                    .forEach(x -> {
                        sqlDuration.updateAndGet(v -> v + x.getWasteTime());
                    });
            result.setLogicDuration(String.valueOf(sqlDuration.get()));
        }
        result.setCurrentThreadId(threadId);
        return result;
    }

}

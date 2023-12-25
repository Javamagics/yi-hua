package com.yihua.common;

import com.yihua.entity.SqlEntity;
import com.yihua.entity.SqlResultEntity;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.util.ObjectUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 关于获取内存相关数据的方法
 *
 * @author wangxusheng
 * @date 2023/11/30 16:44
 * @change 2023/11/30 16:44 by wangxusheng for init
 */
public class MemoryMethods {

    /**
     * getObjectSize
     * 获取某一对象 所占内存空间的实际大小
     *
     * @param o
     * @return java.lang.Long
     * @author wangxusheng
     * @date 2023/11/30 10:26:38
     * @change 2023/11/30 10:26:38 by wangxusheng for init
     * @since 1.0.0
     */
    public static Long getObjectSize(Object o) {
        return RamUsageEstimator.sizeOf(o);
    }


    /**
     * getTotalMemory
     * 获取堆的总内存
     *
     * @return java.lang.Long
     * @author wangxusheng
     * @date 2023/11/30 16:48:53
     * @change 2023/11/30 16:48:53 by wangxusheng for init
     * @since 1.0.0
     */
    public static Long getTotalMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        return totalMemory;
    }

    public static Long getFreeMemory() {
        long freeMemory = Runtime.getRuntime().freeMemory();
        return freeMemory;
    }

    /**
     * getMaxMemory
     * 获取堆的最大内存
     *
     * @return java.lang.Long
     * @author wangxusheng
     * @date 2023/11/30 16:48:53
     * @change 2023/11/30 16:48:53 by wangxusheng for init
     * @since 1.0.0
     */
    public static Long getMaxMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        return maxMemory;
    }

    /**
     * getCurrentSurvivorSpaceFreeMemory
     * 获取当前Survivor区的可用空间
     *
     * @return java.lang.Long
     * @author wangxusheng
     * @date 2023/12/20 10:18:57
     * @change 2023/12/20 10:18:57 by wangxusheng for init
     * @since 1.0.0
     */
    public static Long getCurrentSurvivorSpaceFreeMemory() {
        Long survivorSpaceUsed = 0L;
        Long survivorSpaceInit = 0L;
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            // 检查内存池是否是新生代
            if (pool.getName().endsWith("Survivor Space")) {
                // 获取新生代的内存使用情况
                MemoryUsage usage = pool.getUsage();
                survivorSpaceUsed = usage.getUsed();
                survivorSpaceInit = usage.getMax();
            }
        }
        return survivorSpaceInit - survivorSpaceUsed;
    }

    /**
     * getCurrentEdenSpaceFreeMemory
     * 获取当前Eden区的可用空间
     *
     * @return java.lang.Long
     * @author wangxusheng
     * @date 2023/12/20 10:18:57
     * @change 2023/12/20 10:18:57 by wangxusheng for init
     * @since 1.0.0
     */
    public static Long getCurrentEdenSpaceFreeMemory() {
        Long edenSpaceUsed = 0L;
        Long edenSpaceInit = 0L;
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            // 检查内存池是否是新生代
            if (pool.getName().endsWith("Eden Space")) {
                // 获取新生代的内存使用情况
                MemoryUsage usage = pool.getUsage();
                edenSpaceUsed = usage.getUsed();
                edenSpaceInit = usage.getMax();
            }
        }
        return edenSpaceInit - edenSpaceUsed;
    }

    /**
     * getSqlResultRamUseRate
     * 计算sql返回结果所占内存大小与最大堆内存大小比值
     *
     * @return void
     * @author wangxusheng
     * @date 2023/11/30 17:11:24
     * @change 2023/11/30 17:11:24 by wangxusheng for init
     * @since 1.0.0
     */
    public static void getSqlResultRamUseRate(SqlEntity sqlEntity) {
        //1.获取当前堆的可用内存 如果运行时没有指定Xms，可能会影响最终的结果
        Long freeMemory = getFreeMemory();
        if (!ObjectUtils.isEmpty(sqlEntity)) {
            List<SqlResultEntity> sqlResultList =
                    sqlEntity.getSqlResultEntityList();
            for (SqlResultEntity single : sqlResultList) {
                BigDecimal max = new BigDecimal(freeMemory);
                BigDecimal sqlResultRam = new BigDecimal(single.getResultRamSize());
                BigDecimal rate = sqlResultRam.divide(max, 2, RoundingMode.HALF_UP);
                single.setRamRate(String.valueOf(rate));
                single.setIsBig(judgeIsBigObject(single.getEdenFreeSpace(),
                        single.getSurvivorFreeSpace(),
                        single.getResultRamSize(),rate));
            }
        }
    }

    /**
     * judgeIsBigObject
     * 判断是否为大对象
     * 大对象条件：Eden区的可用内存空间不足以容纳该对象，并且Survivor区也无法容纳，此时将其看作大对象
     * 注意：新生代不足以容纳该对象，会导致一次minor gc，这里把引发minor gc的对象，也看作大对象
     * @param freeEdenFreeSize
     * @param freeSurvivorFreeSpace
     * @param objectSize
     * @return java.lang.Boolean
     * @author wangxusheng
     * @date 2023/12/20 10:42:45
     * @change 2023/12/20 10:42:45 by wangxusheng for init
     * @since 1.0.0
     */
    public static Boolean judgeIsBigObject(Long freeEdenFreeSize,
                                           Long freeSurvivorFreeSpace,
                                           Long objectSize,
                                           BigDecimal rate) {
        BigDecimal rateBound = new BigDecimal("0.00");
        if ((objectSize > freeEdenFreeSize || objectSize > freeSurvivorFreeSpace)&&rate.compareTo(rateBound)==1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}

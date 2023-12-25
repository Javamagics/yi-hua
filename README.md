# Yi-Hua
一款用于检测接口运行时长、sql运行时长及sql返回结果大小，并判断sql返回结果是否为大对象的工具。
本工具的研发目的：力图在测试阶段，将一些可能会由于sql运行导致的OOM的问题、接口效率问题及时检测到，从而反馈给研发同学

## 1.Yi-Hua名称来源
在遥远的Japan省，有一位美丽的演员————星宫一花，其一颦一笑，一举一动，都深深吸引着小王同学，故取名：Yi-Hua

## 2.研发背景
（1）有一天，小王同学在某个项目研发过程中，突然在测试环境碰到了OOM问题，并根据日志诊断为sql查询结果过大导致的。小王本以为这种情况
并不多见，万万没想到，在另一个项目发现，很多刚来的同学都喜欢使用全量查询大表（主表）数据，然后根据大表数据去驱动小表（附属表）。
结果显而易见，在测试环境进行大数据量测试的时候，OOM问题频发，但是由于研发人员流动性过大，代码后期不好维护，故此，小王同学萌生了研发Yi-Hua
的想法。
（2）由于小王所在的学校，人员流动率很大，导致需要去接手并优化不同人写的接口。尤其是优化接口效率方面。但是身边的同学每次都是先在业务层中进行时间的
打印，然后上传代码，然后再由运维的同事帮忙发布，这样就导致很繁琐，尤其是当运维的同学忙的不可开交的时候。所以Yi-Hua也拥有去统计一个接口中sql查询时间
和Java代码逻辑运行时间。

## 参数解释
注解参数解释：
    button:是否开启，默认值 true

引入本工具，需要在项目中的yaml文件进行配置：
api:
    switch:
        global-switch: 注解是否生效（生产环境，不建议打开）默认为true
    log:
        path: Yi-Hua日志输出路径，若不配置，默认与当前项目处于同一目录下
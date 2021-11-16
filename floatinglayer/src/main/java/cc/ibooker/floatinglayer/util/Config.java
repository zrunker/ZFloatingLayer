package cc.ibooker.floatinglayer.util;

import static cc.ibooker.floatinglayer.combination.CombinationEnum.COMBINATION_RIGHT_TO_LEFT_ONE;

import cc.ibooker.floatinglayer.combination.CombinationEnum;

/**
 * @program: ZFloatingLayer
 * @description: 配置信息常量类
 * @author: zoufengli01
 * @create: 2021-10-21 17:26
 **/
public class Config {
    /*初始化View最大数量*/
    public static int maxViewNum = 40;
    /*初始化ViewHolder默认数量*/
    public static int initViewNum = 20;
    /*消息分发时间间隔 ms*/
    public static int msgDelayTimeDiff = 400;
    /*是否开启View缓存*/
    public static boolean isOpenCache = true;
    /*默认动画运行时长*/
    public static int defaultAnimDuration = 3500;
    /*动画运行时长增量，正数增加，负数减少*/
    public static int animDurationIncrement;
    /*动画组合类型,默认组合一*/
    public static CombinationEnum combinationEnum = COMBINATION_RIGHT_TO_LEFT_ONE;
}

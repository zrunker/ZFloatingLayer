package cc.ibooker.floatinglayer.core.flayer.view.strategy;

/**
 * @program: ZFloatingLayer
 * @description: 子View显示位置策略
 * @author: zoufengli01
 * @create: 2021-10-12 10:41
 **/
public @interface LocationStrategy {
    /*Y轴顶部策略一*/
    int TOPY_X_OFFSET = 1;
    /*Y轴顶部策略二*/
    int TOPY_LINE = 2;
    /*Y轴顶部策略三*/
    int TOPY_RANDOM = 3;
    /*Y轴顶部策略四*/
    int LOCATION_RANDOM = 4;
    /*X轴左侧策略*/
    int LEFTX_Y_OFFSET = 5;
}

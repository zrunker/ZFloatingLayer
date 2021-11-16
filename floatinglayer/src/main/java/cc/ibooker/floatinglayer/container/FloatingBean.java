package cc.ibooker.floatinglayer.container;

import android.graphics.Bitmap;

import cc.ibooker.floatinglayer.cache.ViewFloating;
import cc.ibooker.floatinglayer.util.Config;

/**
 * @program: ZFloatingLayer
 * @description: 浮层消息
 * @author: zoufengli01
 * @create: 2021-11-10 16:05
 **/
public class FloatingBean {
    public ViewFloating vFloating;
    /*View位图*/
    public Bitmap bitmap;
    /*View自身宽度*/
    public int width;
    /*View自身高度*/
    public int height;
    /*X轴开始位置*/
    public float fromX;
    /*X轴结束位置*/
    public float toX;
    /*Y轴位置*/
    public float topY;
    /*每次绘制偏移量*/
    public float disDiff;
    /*已经显示总时长*/
    public long showTime;
    /*动画运行时长，默认4000ms*/
    public int animDuration = Config.defaultAnimDuration;
}

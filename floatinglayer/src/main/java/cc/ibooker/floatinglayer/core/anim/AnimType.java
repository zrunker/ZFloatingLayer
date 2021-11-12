package cc.ibooker.floatinglayer.core.anim;

/**
 * @program: ZFloatingLayer
 * @description: 动画种类
 * @author: zoufengli01
 * @create: 2021-10-12 09:55
 **/
public @interface AnimType {
    /*属性动画*/
    String ANIMATOR = "Animator";
    /*补间动画*/
    String ANIMATION = "Animation";
    /*滚动*/
    String SCROLLER = "Scroller";
    /*SurfaceView*/
    String SURFACE = "SurfaceView";
}

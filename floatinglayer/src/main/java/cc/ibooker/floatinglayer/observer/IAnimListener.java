package cc.ibooker.floatinglayer.observer;

import android.view.View;

/**
 * @program: ZFloatingLayer
 * @description: 动画监听
 * @author: zoufengli01
 * @create: 2021-10-12 15:39
 **/
public interface IAnimListener {
    void onAnimStart(View view);

    void onAnimEnd(View view);
}

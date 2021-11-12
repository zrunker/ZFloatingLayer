package cc.ibooker.floatinglayer.core.executor;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.event.FloatingEvent;
import cc.ibooker.floatinglayer.core.flayer.IFloatingLayer;
import cc.ibooker.floatinglayer.core.vholder.IViewHolder;


/**
 * @program: ZFloatingLayer
 * @description: 执行类接口
 * @author: zoufengli01
 * @create: 2021-10-11 11:49
 **/
public interface IExecutor {

    <T> boolean execute(FloatingEvent<T> fEvent);

    /**
     * 获取FloatingLayer
     */
    IFloatingLayer fLayer();

    /**
     * 预加载ViewHolder
     *
     * @param vHolder 待加载ViewHolder
     * @param num     加载数量
     */
    <T> void preViewHolder(@NonNull IViewHolder<T> vHolder, int num);

    /**
     * 浮层展示
     */
    void onShow();

    /**
     * 浮层隐藏
     */
    void onHide();

    /**
     * 浮层销毁
     */
    void onDestroy();

    /**
     * 浮层清屏
     */
    void onClear();
}

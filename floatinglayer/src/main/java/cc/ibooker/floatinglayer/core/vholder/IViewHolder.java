package cc.ibooker.floatinglayer.core.vholder;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * @program: ZFloatingLayer
 * @description: View支持接口
 * @author: zoufengli01
 * @create: 2021-10-11 11:49
 **/
public interface IViewHolder<T> {

    /**
     * 设置name将会走缓存策略
     */
    @NonNull
    String getName();

    void bindData(T data, View view);

    @NonNull
    View create();

}

package cc.ibooker.floatinglayer.core.vholder;

import android.view.View;

/**
 * @program: ZFloatingLayer
 * @description: View状态监听
 * @author: zoufengli01
 * @create: 2021-10-20 15:59
 **/
public interface IViewStateListener {
    /**
     * 初始化
     */
    void onInit(View view);

    /**
     * View添加
     */
    void onAdd(View view);

    /**
     * View移除
     */
    void onRemove(View view);

    /**
     * View注销
     */
    void onUnInit(View view);
}

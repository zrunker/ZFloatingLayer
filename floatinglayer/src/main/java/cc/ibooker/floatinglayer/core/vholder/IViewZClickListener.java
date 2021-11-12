package cc.ibooker.floatinglayer.core.vholder;

import android.view.View;

/**
 * @program: ZFloatingLayer
 * @description: View点击事件监听
 * @author: zoufengli01
 * @create: 2021-10-20 16:50
 **/
public interface IViewZClickListener {
    /**
     * 点击事件
     */
    void onClick(View view);

    /**
     * 长按事件
     */
    boolean onLongClick(View view);
}

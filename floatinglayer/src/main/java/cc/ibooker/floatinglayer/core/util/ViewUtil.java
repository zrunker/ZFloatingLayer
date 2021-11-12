package cc.ibooker.floatinglayer.core.util;

import android.view.View;

import cc.ibooker.floatinglayer.core.anim.executor.scroller.ZView;
import cc.ibooker.floatinglayer.core.anim.executor.surface.ZSurfaceView;

/**
 * @program: ZFloatingLayer
 * @description: View工具类
 * @author: zoufengli01
 * @create: 2021-10-26 15:22
 **/
public class ViewUtil {

    public static View getRootView(View view) {
        View temp = view;
        if (temp instanceof ZView) {
            temp = ((ZView) view).getView();
        } else if (temp instanceof ZSurfaceView) {
            temp = ((ZSurfaceView) view).getView();
        }
        return temp;
    }
}

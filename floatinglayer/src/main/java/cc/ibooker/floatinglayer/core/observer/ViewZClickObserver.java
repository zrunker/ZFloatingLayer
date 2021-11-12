package cc.ibooker.floatinglayer.core.observer;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.ibooker.floatinglayer.core.util.ViewUtil;
import cc.ibooker.floatinglayer.core.vholder.IViewZClickListener;

/**
 * @program: ZFloatingLayer
 * @description: 点击事件监听器
 * @author: zoufengli01
 * @create: 2021-10-21 10:25
 **/
public class ViewZClickObserver implements IViewZClickListener {
    private static volatile ViewZClickObserver instance;
    private Set<IViewZClickListener> listenerSet;

    private ViewZClickObserver() {
    }

    public static ViewZClickObserver getInstance() {
        if (instance == null) {
            synchronized (ViewZClickObserver.class) {
                if (instance == null) {
                    instance = new ViewZClickObserver();
                }
            }
        }
        return instance;
    }

    public void registerListener(@NonNull IViewZClickListener iListener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        this.listenerSet.add(iListener);
    }

    @Override
    public void onClick(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewZClickListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewZClickListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onClick(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewZClickListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewZClickListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onLongClick(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
        return false;
    }
}

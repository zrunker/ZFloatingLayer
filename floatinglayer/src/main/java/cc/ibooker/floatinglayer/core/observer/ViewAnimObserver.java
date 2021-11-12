package cc.ibooker.floatinglayer.core.observer;

import android.view.View;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.util.ViewUtil;

/**
 * @program: ZFloatingLayer
 * @description: 动画监听
 * @author: zoufengli01
 * @create: 2021-10-28 16:48
 **/
public class ViewAnimObserver implements IAnimListener {
    private static volatile ViewAnimObserver instance;
    private Set<IAnimListener> listenerSet;

    private ViewAnimObserver() {
    }

    public static ViewAnimObserver getInstance() {
        if (instance == null) {
            synchronized (ViewAnimObserver.class) {
                if (instance == null) {
                    instance = new ViewAnimObserver();
                }
            }
        }
        return instance;
    }

    public void registerListener(IAnimListener iListener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        this.listenerSet.add(iListener);
    }

    @Override
    public void onAnimStart(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IAnimListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IAnimListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onAnimStart(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onAnimEnd(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IAnimListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IAnimListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onAnimEnd(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}

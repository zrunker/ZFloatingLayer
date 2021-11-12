package cc.ibooker.floatinglayer.core.observer;

import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.ibooker.floatinglayer.core.util.ViewUtil;
import cc.ibooker.floatinglayer.core.vholder.IViewStateListener;

/**
 * @program: ZFloatingLayer
 * @description: View状态监听器
 * @author: zoufengli01
 * @create: 2021-10-20 16:12
 **/
public class ViewStateObserver implements IViewStateListener {
    private static volatile ViewStateObserver instance;
    private Set<IViewStateListener> listenerSet;

    private ViewStateObserver() {
    }

    public static ViewStateObserver getInstance() {
        if (instance == null) {
            synchronized (ViewStateObserver.class) {
                if (instance == null) {
                    instance = new ViewStateObserver();
                }
            }
        }
        return instance;
    }

    public void registerListener(IViewStateListener iListener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        this.listenerSet.add(iListener);
    }

    @Override
    public void onInit(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewStateListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewStateListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onInit(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onUnInit(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewStateListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewStateListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onUnInit(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onAdd(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewStateListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewStateListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onAdd(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onRemove(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            View rootView = ViewUtil.getRootView(view);
            Iterator<IViewStateListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewStateListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onRemove(rootView);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}

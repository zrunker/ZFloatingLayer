package cc.ibooker.floatinglayer.observer;

import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
            Iterator<IAnimListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IAnimListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onAnimStart(view);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void onAnimEnd(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            Iterator<IAnimListener> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IAnimListener iListener = iterator.next();
                if (iListener != null) {
                    iListener.onAnimEnd(view);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}

package cc.ibooker.floatinglayer.observer;

import android.view.View;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: AIChinese
 * @description: View刷新通知
 * @author: zoufengli01
 * @create: 2021/12/23 5:20 下午
 **/
public class ViewRefreshObserver implements IViewRefreshObserver {
    private static volatile ViewRefreshObserver instance;
    private Set<IViewRefreshObserver> listenerSet;

    private ViewRefreshObserver() {
    }

    public static ViewRefreshObserver getInstance() {
        if (instance == null) {
            synchronized (ViewRefreshObserver.class) {
                if (instance == null) {
                    instance = new ViewRefreshObserver();
                }
            }
        }
        return instance;
    }

    public void registerListener(IViewRefreshObserver iListener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        this.listenerSet.add(iListener);
    }

    @Override
    public void onRefresh(View view) {
        if (listenerSet != null && listenerSet.size() > 0 && view != null) {
            Iterator<IViewRefreshObserver> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                IViewRefreshObserver iListener = iterator.next();
                if (iListener != null) {
                    iListener.onRefresh(view);
                } else {
                    iterator.remove();
                }
            }
        }
    }
}

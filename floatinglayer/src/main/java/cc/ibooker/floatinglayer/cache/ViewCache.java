package cc.ibooker.floatinglayer.cache;

import android.text.TextUtils;

import androidx.collection.ArraySet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cc.ibooker.floatinglayer.observer.ViewStateObserver;

/**
 * @program: ZFloatingLayer
 * @description: View缓存
 * @author: zoufengli01
 * @create: 2021-10-12 12:25
 **/
public class ViewCache extends AbsCache {
    private final ConcurrentHashMap<String, Set<ViewFloating>> mMap;

    public ViewCache() {
        super();
        mMap = new ConcurrentHashMap<>();
    }

    @Override
    public ViewFloating getView(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Set<ViewFloating> set = mMap.get(tag);
            if (set != null && set.size() > 0) {
                for (ViewFloating item : set) {
                    if (item.isEnd()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void putView(ViewFloating viewFloating, String tag) {
        if (viewFloating != null && !TextUtils.isEmpty(tag)) {
            Set<ViewFloating> set = mMap.get(tag);
            if (set == null) {
                set = new ArraySet<>();
            }
            set.add(viewFloating);
            mMap.put(tag, set);
            // 清空过期数据
            clearOverdueView(tag);
        }
    }

    /**
     * 清空过期数据
     */
    private void clearOverdueView(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Set<ViewFloating> set = mMap.get(tag);
            if (set != null && set.size() > maxItemSize) {
                Iterator<ViewFloating> iterator = set.iterator();
                while (iterator.hasNext()) {
                    ViewFloating viewFloating = iterator.next();
                    if (viewFloating.isEnd()) {
                        ViewStateObserver.getInstance().onUnInit(viewFloating.view);
                        iterator.remove();
                    }
                }
            }
        }
    }

    /**
     * 清空所有数据
     */
    @Override
    public void clearAll() {
        if (mMap != null && mMap.size() > 0) {
            Set<Map.Entry<String, Set<ViewFloating>>> set = mMap.entrySet();
            Iterator<Map.Entry<String, Set<ViewFloating>>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Set<ViewFloating>> entry = iterator.next();
                Set<ViewFloating> subSet = entry.getValue();
                for (ViewFloating viewFloating : subSet) {
                    ViewStateObserver.getInstance().onUnInit(viewFloating.view);
                }
                iterator.remove();
            }
        }
    }
}

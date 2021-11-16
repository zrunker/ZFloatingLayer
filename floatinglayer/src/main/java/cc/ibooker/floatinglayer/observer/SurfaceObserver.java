package cc.ibooker.floatinglayer.observer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: ZFloatingLayer
 * @description: SurfaceView状态监听
 * @author: zoufengli01
 * @create: 2021-11-11 22:14
 **/
public class SurfaceObserver implements ISurface {
    private static volatile SurfaceObserver instance;
    private Set<ISurface> listenerSet;

    private SurfaceObserver() {
    }

    public static SurfaceObserver getInstance() {
        if (instance == null) {
            synchronized (SurfaceObserver.class) {
                if (instance == null) {
                    instance = new SurfaceObserver();
                }
            }
        }
        return instance;
    }

    public void registerListener(ISurface iListener) {
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
        }
        this.listenerSet.add(iListener);
    }

    @Override
    public void surfaceCreated() {
        if (listenerSet != null && listenerSet.size() > 0) {
            Iterator<ISurface> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                ISurface iListener = iterator.next();
                if (iListener != null) {
                    iListener.surfaceCreated();
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed() {
        if (listenerSet != null && listenerSet.size() > 0) {
            Iterator<ISurface> iterator = listenerSet.iterator();
            while (iterator.hasNext()) {
                ISurface iListener = iterator.next();
                if (iListener != null) {
                    iListener.surfaceDestroyed();
                } else {
                    iterator.remove();
                }
            }
        }
    }
}

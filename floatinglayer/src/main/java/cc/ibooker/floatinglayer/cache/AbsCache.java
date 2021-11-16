package cc.ibooker.floatinglayer.cache;

/**
 * @program: ZFloatingLayer
 * @description: 缓存抽象类
 * @author: zoufengli01
 * @create: 2021-10-12 12:50
 **/
public abstract class AbsCache {
    /*缓存最大数量*/
    protected final int maxItemSize;

    public AbsCache() {
        maxItemSize = 100;
    }

    public AbsCache(int maxItemSize) {
        this.maxItemSize = maxItemSize;
    }

    public abstract ViewFloating getView(String tag);

    public abstract void putView(ViewFloating view, String tag);

    public abstract void clearAll();

}

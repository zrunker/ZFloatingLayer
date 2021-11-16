package cc.ibooker.floatinglayer.executor;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.cache.AbsCache;
import cc.ibooker.floatinglayer.cache.ViewCache;
import cc.ibooker.floatinglayer.cache.ViewFloating;
import cc.ibooker.floatinglayer.event.FloatingEvent;
import cc.ibooker.floatinglayer.event.IEvent;
import cc.ibooker.floatinglayer.flayer.FloatingLayer;
import cc.ibooker.floatinglayer.flayer.IFloatingLayer;
import cc.ibooker.floatinglayer.util.Config;
import cc.ibooker.floatinglayer.vholder.IViewHolder;
import cc.ibooker.floatinglayer.vholder.ViewState;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外执行类
 * @author: zoufengli01
 * @create: 2021-10-12 11:08
 **/
public class FloatingExecutor implements IExecutor {
    private IFloatingLayer fLayer;
    private AbsCache viewCache;

    public FloatingExecutor(Context context) {
        this.fLayer = new FloatingLayer(context);
        this.viewCache = new ViewCache();
    }

    public void setLayer(IFloatingLayer fLayer) {
        this.fLayer = fLayer;
    }

    public void setViewCache(AbsCache viewCache) {
        this.viewCache = viewCache;
    }

    @Override
    public IFloatingLayer fLayer() {
        return fLayer;
    }

    @Override
    public void onShow() {
        if (fLayer != null) {
            fLayer.show();
        }
    }

    @Override
    public void onHide() {
        if (fLayer != null) {
            fLayer.hide();
        }
    }

    @Override
    public void onDestroy() {
        if (viewCache != null) {
            viewCache.clearAll();
        }
        if (fLayer != null) {
            fLayer.destroy();
        }
    }

    @Override
    public void onClear() {
        if (fLayer != null) {
            fLayer.clear();
        }
    }

    @Override
    public <T> void preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
        if (Config.isOpenCache) {
            String name = vHolder.getName();
            if (fLayer != null && viewCache != null
                    && num > 0 && !TextUtils.isEmpty(name)) {
                if (num > Config.maxViewNum) {
                    num = Config.initViewNum;
                }
                ViewFloating temp = viewCache.getView(name);
                if (temp == null) {
                    for (int i = 0; i < num; i++) {
                        // 创建View
                        View view = vHolder.create();
                        ViewFloating viewFloating = new ViewFloating(view);
                        // 设置ViewTag
                        viewFloating.viewState = ViewState.END;
                        // 添加到缓存
                        viewCache.putView(viewFloating, vHolder.getName());
                    }
                }
            }
        }
    }

    @Override
    public <T> boolean execute(FloatingEvent<T> fEvent) {
        if (fEvent != null
                && fEvent.iEvent != null
                && fLayer != null
                && viewCache != null) {
            // 装饰View
            ViewFloating vFloating = packView(fEvent.iEvent);
            if (vFloating != null && vFloating.view != null) {
                // 添加浮层消息
                FloatingMsg fMsg = new FloatingMsg(vFloating,
                        fEvent.combinationEnum.getAnimFunStyle(),
                        fEvent.combinationEnum.getLocationStrategy(),
                        fEvent.animDuration);
                fLayer.addFloatingMsg(fMsg);
                return true;
            }
        }
        return false;
    }

    /**
     * 组合ViewFloating，绑定数据
     */
    private <T> ViewFloating packView(@NonNull IEvent<T> iEvent) {
        IViewHolder<T> vHolder = iEvent.vHolder();
        if (vHolder != null) {
            ViewFloating vFloating;
            if (Config.isOpenCache) {
                String name = vHolder.getName();
                vFloating = viewCache.getView(name);
                if (vFloating == null || vFloating.view == null) {
                    vFloating = createView(vHolder);
                    // 添加到缓存
                    viewCache.putView(vFloating, name);
                }
            } else {
                vFloating = createView(vHolder);
            }
            // 设置初始化状态
            vFloating.updateState(ViewState.INIT);
            // 绑定数据
            vHolder.bindData(iEvent.data(), vFloating.view);
            return vFloating;
        }
        return null;
    }

    private <T> ViewFloating createView(@NonNull IViewHolder<T> vHolder) {
        Log.d("FloatingExecutor", "createView:新生成View");
        View view = vHolder.create();
        return new ViewFloating(view);
    }

}

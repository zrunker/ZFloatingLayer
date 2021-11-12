package cc.ibooker.floatinglayer.core.executor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.AnimService;
import cc.ibooker.floatinglayer.core.anim.AnimType;
import cc.ibooker.floatinglayer.core.anim.executor.scroller.ZView;
import cc.ibooker.floatinglayer.core.cache.AbsCache;
import cc.ibooker.floatinglayer.core.cache.ViewCache;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.event.FloatingEvent;
import cc.ibooker.floatinglayer.core.event.IEvent;
import cc.ibooker.floatinglayer.core.flayer.FloatingLayer;
import cc.ibooker.floatinglayer.core.flayer.IFloatingLayer;
import cc.ibooker.floatinglayer.core.observer.ViewStateObserver;
import cc.ibooker.floatinglayer.core.util.ConstantUtil;
import cc.ibooker.floatinglayer.core.vholder.IViewHolder;
import cc.ibooker.floatinglayer.core.vholder.ViewState;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外执行类
 * @author: zoufengli01
 * @create: 2021-10-12 11:08
 **/
public class FloatingExecutor implements IExecutor {
    private final Context context;
    private IFloatingLayer fLayer;
    private AbsCache viewCache;
    private AnimService animService;

    public FloatingExecutor(Context context) {
        this.context = context;
        this.fLayer = new FloatingLayer(context);
        this.viewCache = new ViewCache();
    }

    public void setLayer(IFloatingLayer fLayer) {
        this.fLayer = fLayer;
    }

    public void setViewCache(AbsCache viewCache) {
        this.viewCache = viewCache;
    }

    public void setAnimService(AnimService animService) {
        this.animService = animService;
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
        String name = vHolder.getName();
        if (fLayer != null && viewCache != null
                && num > 0 && !TextUtils.isEmpty(name)) {
            if (num > ConstantUtil.MAX_VIEWHOLDER_NUM) {
                num = ConstantUtil.DEFAULT_VIEWHOLDER_NUM;
            }
            ViewFloating temp = viewCache.getView(name);
            if (temp == null) {
                for (int i = 0; i < num; i++) {
                    // 创建View
                    View view = vHolder.create();
                    ViewFloating viewFloating = new ViewFloating(view);
                    // 设置ViewTag
                    viewFloating.updateViewState(ViewState.END);
                    // 添加到缓存
                    viewCache.putView(viewFloating, vHolder.getName());
                }
            }
        }
    }

    @Override
    public <T> boolean execute(FloatingEvent<T> fEvent) {
        if (fEvent != null && fLayer != null && viewCache != null) {
            // 设置任务已就绪状态
            fEvent.isReady = true;
            // 装饰View
            ViewFloating viewFloating = decorateView(fEvent);
            if (viewFloating != null && viewFloating.view != null) {
                // 添加View
                fLayer.addView(viewFloating.view);
                // 执行动画
                executeAnim(fEvent, viewFloating);
                return true;
            }
        }
        return false;
    }

    /**
     * 组装View
     */
    private <T> ViewFloating decorateView(@NonNull FloatingEvent<T> fEvent) {
        ViewFloating viewFloating = createView(fEvent.event);
        // 重置View
        if (viewFloating != null && viewFloating.view != null) {
            View view = viewFloating.view;
            // 动画种类
            String animType = fEvent.getAnimType();
            // View重置
            if (AnimType.SCROLLER.equals(animType) && !(view instanceof ZView)) {
                ZView zView = new ZView(view.getContext());
                zView.addView(view);
                viewFloating.view = zView;
            }
//            else if (AnimType.SURFACE.equals(animType) && !(view instanceof ZSurfaceView)) {
//                ZSurfaceView zSurfaceView = new ZSurfaceView(view.getContext());
//                zSurfaceView.addView(view);
//                viewFloating.view = zSurfaceView;
//            }
            return viewFloating;
        }
        return null;
    }

    /**
     * 创建View，绑定数据
     */
    private <T> ViewFloating createView(@NonNull IEvent<T> event) {
        IViewHolder<T> vHolder = event.vHolder();
        if (vHolder != null) {
            String name = vHolder.getName();
            ViewFloating viewFloating = viewCache.getView(name);
            if (viewFloating == null || viewFloating.view == null) {
                Log.d("FloatingExecutor", "createView:需要新生成View");
                View view = vHolder.create();
                viewFloating = new ViewFloating(view);
                // 添加到缓存
                viewCache.putView(viewFloating, name);
            }
            // 设置初始化状态
            viewFloating.updateViewState(ViewState.INIT);
            // 绑定数据
            vHolder.bindData(event.data(), viewFloating.view);
            // 发送通知
            ViewStateObserver.getInstance().onInit(viewFloating.view);
            return viewFloating;
        }
        return null;
    }

    /**
     * 执行动画
     */
    private <T> void executeAnim(final FloatingEvent<T> fEvent, ViewFloating viewFloating) {
        if (animService == null) {
            animService = new AnimService() {

                @Override
                protected void animInitBack(@NonNull ViewFloating vFloating,
                                            @NonNull FloatingEvent<?> event) {
                    // 重置View位置
                    fLayer.resetChildViewLocation(vFloating.view,
                            event.combinationEnum.getLocationStrategy());
                }

                @Override
                protected void animStartBack(@NonNull ViewFloating vFloating,
                                             @NonNull FloatingEvent<?> event) {

                }

                @Override
                protected void animEndBack(@NonNull ViewFloating vFloating,
                                           @NonNull FloatingEvent<?> event) {

                }
            };
        }
        animService.executeAnim(viewFloating, fEvent, fLayer.fLayerConfig());
    }

}

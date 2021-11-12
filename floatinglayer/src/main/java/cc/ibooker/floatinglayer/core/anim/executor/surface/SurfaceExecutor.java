package cc.ibooker.floatinglayer.core.anim.executor.surface;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.anim.executor.AbsAnimExecutor;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.core.flayer.view.FloatingLayout;

/**
 * @program: ZFloatingLayer
 * @description: 基于SurfaceView实现动画
 * @author: zoufengli01
 * @create: 2021-10-22 16:51
 **/
public class SurfaceExecutor extends AbsAnimExecutor {

    public SurfaceExecutor(String animFunStyle) {
        super(animFunStyle);
    }

    @Override
    public void execute(@NonNull ViewFloating viewFloating,
                        @NonNull FLayerConfig fLayerConfig,
                        @NonNull final IAnimListener listener) {
//        View view = viewFloating.view;
//        if (view != null) {
//            final ViewParent viewParent = view.getParent();
//            if (viewParent instanceof FloatingLayout) {
//                final ViewGroup parentView = (ViewGroup) viewParent;
//                // 显示ZSurfaceView
//                final ZSurfaceView zSurfaceView;
//                if (view instanceof ZSurfaceView) {
//                    zSurfaceView = (ZSurfaceView) view;
//                } else {
//                    zSurfaceView = new ZSurfaceView(view.getContext());
//                    parentView.removeView(view);
//                    zSurfaceView.addView(view);
//                    parentView.addView(zSurfaceView);
//                }
//
//                // 重置ZSurfaceView宽高
//                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//                if (params == null) {
//                    params = new ViewGroup.MarginLayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                } else {
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                }
//                params.leftMargin = 0;
//                params.rightMargin = 0;
//                zSurfaceView.setLayoutParams(params);
//                zSurfaceView.setZOrderOnTop(true);
//
//                // 执行动画
//                zSurfaceView.startAnim(view, fLayerConfig, new IAnimListener() {
//                    @Override
//                    public void onAnimStart() {
//                        listener.onAnimStart();
//                    }
//
//                    @Override
//                    public void onAnimEnd() {
//                        parentView.removeView(zSurfaceView);
//                        listener.onAnimEnd();
//                    }
//                });
//            }
//        }

        final View view = viewFloating.view;
        if (view != null) {
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof FloatingLayout) {
                // 移除ViewParent
                final ViewGroup viewGroup = (FloatingLayout) viewParent;
                viewGroup.removeView(view);

                // 生成ZSurfaceView
                final ZSurfaceView zSurfaceView = new ZSurfaceView(view.getContext());
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                viewGroup.addView(zSurfaceView, params);

                // 执行动画
                zSurfaceView.startAnim(view, fLayerConfig, new IAnimListener() {
                    @Override
                    public void onAnimStart(View v) {
                        listener.onAnimStart(v);
                    }

                    @Override
                    public void onAnimEnd(View v) {
                        viewGroup.removeView(zSurfaceView);
                        listener.onAnimEnd(v);
                    }
                });
            }
        }

    }
}

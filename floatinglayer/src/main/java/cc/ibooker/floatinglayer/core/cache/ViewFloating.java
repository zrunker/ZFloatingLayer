package cc.ibooker.floatinglayer.core.cache;

import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.observer.ViewZClickObserver;
import cc.ibooker.floatinglayer.core.vholder.ViewState;
import cc.ibooker.floatinglayer.core.vholder.ViewTag;

/**
 * @program: ZFloatingLayer
 * @description: View包装类
 * @author: zoufengli01
 * @create: 2021-10-25 20:46
 **/
public class ViewFloating {
    public View view;
    public ViewTag viewTag;

    public ViewFloating(@NonNull View view) {
        this.view = view;
        // 设置测量模式
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);
        // 注册点击事件
        this.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewZClickObserver.getInstance().onClick(v);
            }
        });
        this.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return ViewZClickObserver.getInstance().onLongClick(v);
            }
        });
    }

    /**
     * 更新View状态
     */
    public void updateViewState(@ViewState int viewState) {
        // 更新ViewTag
        if (viewTag == null) {
            viewTag = new ViewTag();
        }
        viewTag.viewState = viewState;
        // 修改View显示状态
        switch (viewState) {
            case ViewState.INIT:
                view.setVisibility(View.INVISIBLE);
                break;
            case ViewState.START:
                view.setVisibility(View.VISIBLE);
                break;
            case ViewState.END:
                if (view.getAnimation() != null) {
                    view.clearAnimation();
                }
                view.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 停止动画
     */
    public void stopAnim() {
        if (viewTag != null) {
            viewTag.stopAnim();
        }
    }

    /**
     * 是否结束
     */
    public boolean isEnd() {
        return viewTag != null && ViewState.END == viewTag.viewState;
    }
}

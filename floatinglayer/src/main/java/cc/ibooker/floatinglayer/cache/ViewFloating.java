package cc.ibooker.floatinglayer.cache;

import android.view.View;

import cc.ibooker.floatinglayer.observer.ViewAnimObserver;
import cc.ibooker.floatinglayer.observer.ViewStateObserver;
import cc.ibooker.floatinglayer.vholder.ViewState;

/**
 * @program: ZFloatingLayer
 * @description: View包装类
 * @author: zoufengli01
 * @create: 2021-10-25 20:46
 **/
public class ViewFloating {
    public View view;

    @ViewState
    public int viewState = ViewState.INIT;

    public ViewFloating(View view) {
        this.view = view;
    }

    public boolean isEnd() {
        return viewState == ViewState.END;
    }

    public void updateState(@ViewState int viewState) {
        this.viewState = viewState;
        switch (viewState) {
            case ViewState.INIT:
                ViewStateObserver.getInstance().onInit(view);
                break;
            case ViewState.START:
                ViewAnimObserver.getInstance().onAnimStart(view);
                ViewStateObserver.getInstance().onAdd(view);
                break;
            case ViewState.END:
                ViewAnimObserver.getInstance().onAnimEnd(view);
                ViewStateObserver.getInstance().onRemove(view);
                break;
        }
    }
}

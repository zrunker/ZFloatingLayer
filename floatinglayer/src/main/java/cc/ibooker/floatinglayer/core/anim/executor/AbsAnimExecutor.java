package cc.ibooker.floatinglayer.core.anim.executor;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.AnimFunStyle;
import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 动画执行接口类
 * @author: zoufengli01
 * @create: 2021-10-12 11:34
 **/
public abstract class AbsAnimExecutor {
    @AnimFunStyle
    protected final String animFunStyle;

    public AbsAnimExecutor(@AnimFunStyle String animFunStyle) {
        this.animFunStyle = animFunStyle;
    }

    public abstract void execute(@NonNull ViewFloating viewFloating,
                                 @NonNull FLayerConfig fLayerConfig,
                                 @NonNull IAnimListener listener);

}

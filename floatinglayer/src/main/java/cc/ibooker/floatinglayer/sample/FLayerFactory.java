package cc.ibooker.floatinglayer.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

import cc.ibooker.floatinglayer.R;
import cc.ibooker.floatinglayer.core.FloatingService;
import cc.ibooker.floatinglayer.core.vholder.IViewHolder;
import cc.ibooker.floatinglayer.sample.dto.SendEnum;
import cc.ibooker.floatinglayer.sample.view.IView;
import cc.ibooker.floatinglayer.sample.view.ViewType;

/**
 * @program: ZFloatingLayer
 * @description: 浮层工厂类
 * @author: zoufengli01
 * @create: 2021-10-12 16:28
 **/
public class FLayerFactory {
    private static final ConcurrentHashMap<SendEnum, FloatingService.Proxy<?>> MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<SendEnum, FloatingService.Proxy<?>> getMap() {
        return MAP;
    }

    public static <T> FloatingService.Proxy<T> createBuilder(@NonNull Context context,
                                                             @NonNull SendEnum sendEnum) {
        if (MAP.get(sendEnum) != null) {
            return (FloatingService.Proxy<T>) MAP.get(sendEnum);
        } else {
            IViewHolder<T> vHolder = createViewHolder(context, sendEnum);
            FloatingService.Proxy<T> proxy =
                    new FloatingService.Proxy<>(context, vHolder)
                            .preViewHolder(vHolder, 10);
            MAP.put(sendEnum, proxy);
            return proxy;
        }
    }

    private static <T> IViewHolder<T> createViewHolder(@NonNull final Context context,
                                                       @NonNull final SendEnum sendEnum) {
        return new IViewHolder<T>() {
            @NonNull
            @Override
            public View create() {
                switch (sendEnum.getViewType()) {
                    case ViewType.VIEW_RIGHT_TO_LEFT_1:
                        return LayoutInflater.from(context.getApplicationContext())
                                .inflate(R.layout.sample_layout_right_to_left, null);
                    case ViewType.VIEW_SHOW_TO_HIDE_1:
                        return LayoutInflater.from(context.getApplicationContext())
                                .inflate(R.layout.sample_layout_show_to_hide, null);
                }
                return null;
            }

            @Override
            public void bindData(T data, View view) {
                if (view instanceof IView) {
                    ((IView<T>) view).bindData(data);
                }
            }

            @NonNull
            @Override
            public String getName() {
                return sendEnum.getViewType();
            }
        };
    }
}

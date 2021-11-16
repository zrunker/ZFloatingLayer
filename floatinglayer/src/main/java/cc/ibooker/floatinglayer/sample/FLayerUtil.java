package cc.ibooker.floatinglayer.sample;


import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cc.ibooker.floatinglayer.FloatingService;
import cc.ibooker.floatinglayer.sample.dto.SendEnum;
import cc.ibooker.floatinglayer.sample.dto.SendRequest;

/**
 * @program: ZFloatingLayer
 * @description: 浮层工具类
 * @author: zoufengli01
 * @create: 2021-10-11 16:40
 **/
public class FLayerUtil {
    private final Context context;

    public FLayerUtil(@NonNull Context context) {
        this.context = context;
    }

    /**
     * 发送事件
     */
    public <T> void send(SendRequest<T> data) {
        SendEnum sendEnum = SendEnum.getValue(data.sendType);
        if (sendEnum != null) {
            FloatingService.Proxy<T> proxy = FLayerFactory.createBuilder(context, sendEnum);
            if (proxy != null) {
                proxy.send(data.data, sendEnum.getCombinationEnum());
            }
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        ConcurrentHashMap<SendEnum, FloatingService.Proxy<?>> mMap = FLayerFactory.getMap();
        Set<Map.Entry<SendEnum, FloatingService.Proxy<?>>> set = mMap.entrySet();
        for (Map.Entry<SendEnum, FloatingService.Proxy<?>> entry : set) {
            entry.getValue().destroy();
        }
    }

    /**
     * 展示
     */
    public void show() {
        ConcurrentHashMap<SendEnum, FloatingService.Proxy<?>> mMap = FLayerFactory.getMap();
        Set<Map.Entry<SendEnum, FloatingService.Proxy<?>>> set = mMap.entrySet();
        for (Map.Entry<SendEnum, FloatingService.Proxy<?>> entry : set) {
            entry.getValue().show();
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        ConcurrentHashMap<SendEnum, FloatingService.Proxy<?>> mMap = FLayerFactory.getMap();
        Set<Map.Entry<SendEnum, FloatingService.Proxy<?>>> set = mMap.entrySet();
        for (Map.Entry<SendEnum, FloatingService.Proxy<?>> entry : set) {
            entry.getValue().hide();
        }
    }
}

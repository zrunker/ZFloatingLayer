package cc.ibooker.floatinglayer.core.event;


import cc.ibooker.floatinglayer.core.vholder.IViewHolder;

/**
 * @program: ZFloatingLayer
 * @description: 事件接口
 * @author: zoufengli01
 * @create: 2021-10-12 09:51
 **/
public interface IEvent<T> {

    IViewHolder<T> vHolder();

    T data();

}

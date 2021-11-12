package cc.ibooker.floatinglayer.sample.dto;

/**
 * @program: ZFloatingLayer
 * @description: 浮层数据包装类
 * @author: zoufengli01
 * @create: 2021-10-12 16:31
 **/
public class SendRequest<T> {
    @SendType
    public String sendType;

    public T data;
}

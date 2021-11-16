package cc.ibooker.floatinglayer.sample.view;

/**
 * @program: ZFloatingLayer
 * @description: View接口
 * @author: zoufengli01
 * @create: 2021-10-15 12:45
 **/
public interface IView<T> {

    void bindData(T data);
}

package cc.ibooker.floatinglayer.sample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cc.ibooker.floatinglayer.R;


/**
 * @program: ZFloatingLayer
 * @description: 淡入淡出View
 * @author: zoufengli01
 * @create: 2021-10-15 16:10
 **/
public class ShowToHideView<T> extends FrameLayout implements IView<T> {
    private TextView tv;

    public ShowToHideView(Context context) {
        this(context, null);
    }

    public ShowToHideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowToHideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        tv = findViewById(R.id.tv);
    }

    @Override
    public void bindData(T data) {
        if (tv != null) {
            tv.setText(data.toString());
        }
    }
}

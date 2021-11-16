package cc.ibooker.floatinglayer.sample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cc.ibooker.floatinglayer.R;

/**
 * @program: ZFloatingLayer
 * @description: 从右向左View
 * @author: zoufengli01
 * @create: 2021-10-12 16:05
 **/
public class RightToLeftView<T> extends LinearLayout implements IView<T> {
    private TextView tv;

    public RightToLeftView(Context context) {
        this(context, null);
    }

    public RightToLeftView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightToLeftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

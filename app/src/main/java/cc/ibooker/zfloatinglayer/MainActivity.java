package cc.ibooker.zfloatinglayer;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cc.ibooker.floatinglayer.FloatingService;
import cc.ibooker.floatinglayer.flayer.FLayerRegion;
import cc.ibooker.floatinglayer.util.Config;
import cc.ibooker.floatinglayer.util.ScreenUtil;
import cc.ibooker.floatinglayer.vholder.IViewHolder;

public class MainActivity extends AppCompatActivity {
    private TextView tvCurrentAnimDuration;

    private FloatingService.Proxy<String> proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                proxy.clear();
//            }
//        }, 15000);


//        // 方式一
//        FLayerUtil fLayerUtil = new FLayerUtil(this);
//        for (int i = 0; i < 1000; i++) {
//            SendRequest<String> sendRequest = new SendRequest<>();
//            sendRequest.sendType = SendType.SEND_TYPE_ONE;
//            sendRequest.data = "测试-++=测试" + i;
//            fLayerUtil.send(sendRequest);
//        }

        IViewHolder<String> viewHolder = getVH();

//        // 方式二
//        IExecutor iExecutor = new FloatingExecutorProxy(this);

//        // 方式三
//        FloatingService floatingService = new FloatingService(this);

        // 方式四
        proxy = new FloatingService.Proxy<>(this, viewHolder);
        proxy.size(ScreenUtil.getScreenW(this), ScreenUtil.getScreenH(this) - 400);
//        proxy.region(FLayerRegion.SCREEN_CENTER);
//        proxy.location(400, 100);

        for (int i = 0; i < 1000; i++) {
//            int finalI = i;
//            IEvent<String> iEvent = new IEvent<String>() {
//                @Override
//                public IViewHolder<String> vHolder() {
//                    return viewHolder;
//                }
//
//                @Override
//                public String data() {
//                    return "测试-++=" + finalI;
//                }
//            };

//            iExecutor.execute(new FloatingEvent<>(iEvent));

//            floatingService.send(iEvent);

            proxy.send("测试-++=" + i);
        }

    }

    private void initView() {
        tvCurrentAnimDuration = findViewById(R.id.tv_current_anim_duration);
        tvCurrentAnimDuration.setText("单次动画时长：ms" + (Config.defaultAnimDuration + Config.animDurationIncrement));

        findViewById(R.id.tv_add_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.animDurationIncrement -= 100;
                tvCurrentAnimDuration.setText("单次动画时长：ms" + (Config.defaultAnimDuration + Config.animDurationIncrement));
            }
        });

        findViewById(R.id.tv_red_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.animDurationIncrement += 100;
                tvCurrentAnimDuration.setText("单次动画时长：ms" + (Config.defaultAnimDuration + Config.animDurationIncrement));
            }
        });

        findViewById(R.id.tv_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.show();
            }
        });

        findViewById(R.id.tv_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxy.show();
            }
        });

        TextView delayTimeTv = findViewById(R.id.tv_delay_time);
        delayTimeTv.setText("延迟时长：" + Config.msgDelayTimeDiff);

        findViewById(R.id.tv_dt_add_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.msgDelayTimeDiff += 10;
                delayTimeTv.setText("延迟时长：" + Config.msgDelayTimeDiff);
            }
        });

        findViewById(R.id.tv_dt_red_100).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.msgDelayTimeDiff -= 10;
                delayTimeTv.setText("延迟时长：" + Config.msgDelayTimeDiff);
            }
        });
    }


    public IViewHolder<String> getVH() {
        return new IViewHolder<String>() {
            @NonNull
            @Override
            public String getName() {
                return "cacheName";
            }

            @Override
            public void bindData(String data, View view) {
                TextView tv = view.findViewById(R.id.tv);
                tv.setText(data + "");
            }

            @NonNull
            @Override
            public View create() {
                return LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_temp, null);
            }
        };
    }
}
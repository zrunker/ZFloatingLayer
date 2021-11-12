package cc.ibooker.zfloatinglayer;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import cc.ibooker.floatinglayer.sample.FLayerUtil;
import cc.ibooker.floatinglayer.sample.dto.SendRequest;
import cc.ibooker.floatinglayer.sample.dto.SendType;

public class MainActivity extends AppCompatActivity {
    private FLayerUtil fLayerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fLayerUtil = new FLayerUtil(this);

        SendRequest<String> sendRequest = new SendRequest<>();
        sendRequest.sendType = SendType.SEND_TYPE_ONE;
        Handler handler = new Handler();
        send(handler, fLayerUtil, sendRequest, 0);
    }

    private void send(Handler handler, FLayerUtil fLayerUtil,
                      SendRequest<String> sendRequest, int i) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendRequest.data = "我在想象-" + i;
                fLayerUtil.send(sendRequest);
                if (i < 1000) {
                    send(handler, fLayerUtil, sendRequest, i + 1);
                }
            }
        }, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fLayerUtil.destroy();
    }
}
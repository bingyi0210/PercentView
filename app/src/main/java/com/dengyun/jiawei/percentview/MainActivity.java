package com.dengyun.jiawei.percentview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private LinearLayout ll_content;
    private int percent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        final PercentView percentView = new PercentView(this);
        ll_content.addView(percentView);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 110){
                    percent++;
                    percentView.changePercent(percent);

                }
            }
        };


        percentView.setOnBtnClickListener(new PercentView.OnBtnClickListener() {
            @Override
            public void onPercentUp() {

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (percent<85){

                            handler.sendEmptyMessageDelayed(110,0);

                        }else {
                            this.cancel();
                            percent =0;
                        }
                    }
                },0,50);
            }
        });

    }

}

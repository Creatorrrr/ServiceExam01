package com.example.kosta.serviceexam01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = (VideoView)findViewById(R.id.video);

        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);

        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        videoView.requestFocus();
        videoView.start();

//        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,MusicService.class);
//                startService(intent);
//            }
//        });
//
//        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,MusicService.class);
//                stopService(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if(bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                String format = bundle.getString("format");

                SmsMessage msg = SmsMessage.createFromPdu((byte[])pdus[0], format);

                if(msg.getMessageBody().equals("start")) {
                    Intent intentMusic = new Intent(MainActivity.this,MusicService.class);
                    startService(intentMusic);
                } else if(msg.getMessageBody().equals("stop")){
                    Intent intentMusic = new Intent(MainActivity.this,MusicService.class);
                    stopService(intentMusic);
                }
            }
        }
    };
}

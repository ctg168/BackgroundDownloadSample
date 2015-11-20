package com.terry.backgrounddownloadsample;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.UUID;

import static com.terry.backgrounddownloadsample.LogUtil.LogE;

public class MainActivity extends AppCompatActivity {
    private DownloadService downloadService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogE("MainActivity.onServiceConnected");
            downloadService = ((DownloadService.MyBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogE("MainActivity.onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getBaseContext(), DownloadService.class);
        startService(intent);

        //BIND SERVICE
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DownloadService.class);
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
            }
        });

        //UNBIND SERVICE
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
            }
        });

        //CREATE A TIP
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                JetNotificationManager.showTip(MainActivity.this, String.format("%s: %s", 20, UUID.randomUUID().toString()));
            }
        });

        //START DOWNLOAD
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });

        //CLOSE NOTIFICATION VIEW
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShowProgressNotification();
            }
        });
    }


    private void startDownload(){
        DownloadService.DownloadTask downloadTask = new DownloadService.DownloadTask();
        downloadTask.TaskName = UUID.randomUUID().toString();
        downloadTask.Total = 20;
        downloadTask.progress = 0;
        downloadService.startDownLoad(downloadTask);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}

package com.terry.backgrounddownloadsample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.UUID;

import static com.terry.backgrounddownloadsample.LogUtil.LogE;

public class DownloadService extends Service {
    private static final String TAG = "TDService";

    public DownloadService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        final String uuid = UUID.randomUUID().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        Log.d(TAG, uuid);
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startDownLoad() {
        final String uuid = UUID.randomUUID().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        Log.d("startDownLoad", uuid);
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        LogE("TDService.onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        LogE("TDService.onBind");
        return mBinder;
    }

    private MyBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }


}

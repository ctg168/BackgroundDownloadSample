package com.terry.backgrounddownloadsample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import static com.terry.backgrounddownloadsample.LogUtil.LogE;

public class DownloadService extends Service {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private static final String TAG = "TDService";

    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void startDownLoad(DownloadTask downloadTask) {
        DownloadThread downloadThread = new DownloadThread(downloadTask);
        downloadThread.execute();
    }

    //接收DownloadThread里面传过来的值，并输出到控制台
    public void updateProgress(String... values) {
        LogE(String.format("%s: %s/%s (%s)", values[0], values[1], values[2], values[3]));

    }

    private void showNotify(String status) {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.noti_download_panel);
        // setting remoteview init style.
        //...

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), 0);

        mBuilder.setContent(mRemoteViews)
                .setContentIntent(pendingIntent)
                .setTicker("down...");

//        Notification notify = mBuilder.build();
//        notify.contentView = mRemoteViews;

        mNotificationManager.notify(0, mBuilder.build());


    }

    private void setNotify(int progress) {
        mBuilder.setProgress(100, progress, false);//显示进度条
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        LogE("TDService.onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        LogE("DownloadService.onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogE("DownloadService.onUnbind");

        return super.onUnbind(intent);
    }

    private MyBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }


    public static class DownloadTask {
        public DownloadTask() {
        }

        public final static int DownloadState_STOP = 0;
        public final static int DownloadState_INPROGRESS = 1;
        public final static int DownloadState_PAUSED = 2;

        public int Total = 100;
        public int progress = 10;
        public String TaskName = "";
        public int DownloadStatus = DownloadState_STOP;
    }


    class DownloadThread extends AsyncTask<String, String, Object> {
        private DownloadTask downloadTask;

        public DownloadThread(DownloadTask downloadTask) {
            this.downloadTask = downloadTask;
        }

        @Override
        protected Object doInBackground(String... params) {
            while (downloadTask.progress++ < downloadTask.Total) {
                try {
                    publishProgress(
                            downloadTask.TaskName,
                            String.valueOf(downloadTask.progress),
                            String.valueOf(downloadTask.Total),
                            "DownloadState_INPROGRESS"
                    );

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            downloadTask.DownloadStatus = DownloadTask.DownloadState_INPROGRESS;
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length == 4) {
                updateProgress(values);
            }
            super.onProgressUpdate(values);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}

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

    private static final String TAG = "TDService";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private MyBinder mBinder = new MyBinder();

    private DownloadTask mDownloadTask;

    public DownloadService() {

    }

    public void startDownLoad(DownloadTask downloadTask) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        this.mDownloadTask = downloadTask;
        DownloadThread downloadThread = new DownloadThread(downloadTask);
        downloadThread.execute();
    }

    //接收DownloadThread里面传过来的值，并输出到控制台
    public void updateProgress(String... values) {
        LogE(String.format("%s: %s/%s (%s)", values[0], values[1], values[2], values[3]));
        setNotify();
    }

    private void setNotify() {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.noti_download_panel);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), 0);

        if (mDownloadTask.DownloadStatus == DownloadState.STATE_INPROGRESS) {
            mRemoteViews.setProgressBar(R.id.progressBar, mDownloadTask.Total, mDownloadTask.Progress, false);
        } else if (mDownloadTask.DownloadStatus == DownloadState.STATE_STOP) {

        } else {

        }

        mBuilder.setContent(mRemoteViews)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_action_download)
                .setTicker("download started ...");

        Notification notify = mBuilder.build();
        notify.contentView = mRemoteViews;
        mNotificationManager.notify(0, notify);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogE("TDService.onCreate");

//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        mBuilder = new NotificationCompat.Builder(this);

    }

    @Override
    public void onDestroy() {
        LogE("TDService.onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogE("DownloadService.onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogE("DownloadService.onUnbind");
        return super.onUnbind(intent);
    }

    enum DownloadState {
        STATE_STOP,
        STATE_INPROGRESS,
        STATE_PAUSE
    }

    public static class DownloadTask {
        public int Total = 100;
        public int Progress = 10;
        public String TaskName = "";
        public DownloadState DownloadStatus = DownloadState.STATE_STOP;

        public DownloadTask() {
        }
    }

    public class MyBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    class DownloadThread extends AsyncTask<String, String, Object> {
        private DownloadTask downloadTask;

        public DownloadThread(DownloadTask downloadTask) {
            this.downloadTask = downloadTask;
        }

        @Override
        protected Object doInBackground(String... params) {
            while (downloadTask.Progress++ < downloadTask.Total) {
                try {
                    publishProgress(
                            downloadTask.TaskName,
                            String.valueOf(downloadTask.Progress),
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

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param o The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            downloadTask.DownloadStatus = DownloadState.STATE_STOP;
            setNotify();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadTask.DownloadStatus = DownloadState.STATE_INPROGRESS;
            setNotify();
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
            downloadTask.DownloadStatus = DownloadState.STATE_STOP;
            super.onCancelled();
        }
    }
}

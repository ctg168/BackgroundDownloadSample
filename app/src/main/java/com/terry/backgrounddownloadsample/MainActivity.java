package com.terry.backgrounddownloadsample;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;


import java.util.UUID;

import javax.xml.transform.Result;

import static com.terry.backgrounddownloadsample.LogUtil.LogE;

public class MainActivity extends AppCompatActivity {

    private DownloadService downloadService;
    private int tipCount = 1;


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

        //SHOW NOTIFICATION VIEW
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ClickToOpen_Notificaion(MainActivity.this);
                //ShowCompleteView(MainActivity.this);
                //downloadService.startDownLoad();
                DownloadService.DownloadTask downloadTask = new DownloadService.DownloadTask();

                downloadTask.TaskName = UUID.randomUUID().toString();
                downloadTask.Total = 20;
                downloadTask.progress = 0;

                downloadService.startDownLoad(downloadTask);

            }
        });

        //CLOSE NOTIFICATION VIEW
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShowProgressNotification();
                showMedia(MainActivity.this);

            }
        });
        ;
    }

    //normal view with action.
    private void ShowCompleteView(Context context) {

        int defaultTipId = 2001;
        int defaultNotiIcon = R.drawable.ic_noti_icon;
        String defaultTitle = "来自佳腾培训系统的通知";

        if (context == MainActivity.this) {
            System.out.println("MainActivity.ShowCompleteView");
        }

        Intent resultIntent = new Intent(context, ResultActivity.class);

        resultIntent.putExtra("Action", "Play");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(defaultNotiIcon)
                .setLargeIcon(largeIcon)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Normal notification")
                .setContentText("this is an exmaples of a NORMAL style.")
                .addAction(R.drawable.ic_action_playback_play, "", resultPendingIntent)
                .addAction(R.drawable.ic_action_playback_prev, "", resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(defaultTipId, mBuilder.build());

    }

    //With big view without action.
    private void ShowCompleteView2(Context context) {

        int defaultTipId = 2001;
        int defaultNotiIcon = R.drawable.ic_noti_icon;
        String defaultTitle = "来自佳腾培训系统的通知";

        NotificationCompat.BigPictureStyle notiStyle = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle("Big Text Expanded");
        notiStyle.setSummaryText("Nice big text.");

        Bitmap remote_picture = BitmapFactory.decodeResource(getResources(), R.drawable.aliwall_icon);

        CharSequence bigText = "This is an example of a large string to demo how much " +
                "text you can show in a 'Big Text Style' notification.";
        notiStyle.setSummaryText(bigText);

        notiStyle.bigPicture(remote_picture);


        Intent resultIntent = new Intent(context, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(defaultNotiIcon)
                .setAutoCancel(true)
                .setLargeIcon(remote_picture)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Normal notification")
                .setContentText("this is an exmaples of a NORMAL style.")
                .setStyle(notiStyle);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(defaultTipId, mBuilder.build());

    }


    private void CustomViewAndServiceControl(Context context) {

    }

    public void showButtonNotify() {

    }


    private void showMedia(Context context) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.noti_normal_view);
//
//        MediaSessionCompat mediaSession = null;
//
//        Notification noti_normal_view = new Notification.Builder(context)
//                .setSmallIcon(R.drawable.ic_noti_icon)
//                .setContentTitle("Track title")
//                .setContentText("Artist - Album")
//                .setLargeIcon(bitmap)
//                .setStyle(new Notification.MediaStyle())
//                        //.setMediaSession(mediaSession))
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(222, noti_normal_view);
    }

    private void ClickToOpen_Notificaion(Context context) {


        int defaultTipId = 2001;
        int defaultNotiIcon = R.drawable.ic_noti_icon;
        String defaultTitle = "来自佳腾培训系统的通知";

        Intent resultIntent = new Intent(this, ResultActivity.class);

        resultIntent.putExtra(getPackageName() + ".RequestCode", 1);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent1 = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = stackBuilder.getPendingIntent(2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent3 = stackBuilder.getPendingIntent(3, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(defaultNotiIcon)
                .setContentTitle(defaultTitle)
                .setContentText("text")
                .addAction(R.drawable.ic_action_playback_prev, "prev", pendingIntent1)
                .addAction(R.drawable.ic_action_playback_next, "next", pendingIntent2)
                .addAction(R.drawable.ic_action_playback_play, "play", pendingIntent3);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(defaultTipId, mBuilder.build());


        //notificationManager.cancel(defaultTipId); //取消某个通知

    }

    private RemoteViews getRView(Context context) {

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.noti_normal_view);


        contentView.setImageViewResource(R.id.img_noti, R.drawable.noti);

        contentView.setTextViewText(R.id.txt_noti_title, "提示");
        //contentView.setTextViewTextSize(R.id.txt_noti_title, TypedValue.COMPLEX_UNIT_SP, 16.0F);

        contentView.setTextViewText(R.id.txt_noti_body, "您有新的消息！");
        //contentView.setTextViewTextSize(R.id.txt_noti_body, TypedValue.COMPLEX_UNIT_SP, 12.0F);

        return contentView;
    }


    private void ShowPlayerView(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(context);
//
//        //Notification.Action.Builder builder1 = new Notification.Action.Builder()
//
//        PendingIntent prevPendingIntent = PendingIntent.getActivity(context, 0, )
//
//        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
//                .setSmallIcon(R.drawable.ic_stat_time)
//                .addAction(R.drawable.ic_action_playback_prev, "Prev", )


        //PendingIntent pending = PendingIntent.getActivity(context, 3, new Intent(), 0);


    }

    public void ShowProgressNotification(Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_noti_icon);
        new Thread(
                new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            builder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            notificationManager.notify(0, builder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d("aaa", "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        builder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        notificationManager.notify(0, builder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();

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

package com.terry.backgrounddownloadsample;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

public class JetNotificationManager {

    public static int defaultTipId = 2001;
    public static int defaultNotiIcon = R.drawable.ic_noti_icon;
    public static String defaultTitle = "来自佳腾培训系统的通知";

    static {
        System.out.println("JetNotificationManager.static initializer");
    }

    public static void showTip(Context context, String info) {
        showTip(context, defaultTipId, defaultNotiIcon, defaultTitle, info);
    }

    public static void showTip(Context context, int imgRes, String info) {
        showTip(context, imgRes, defaultTitle, info);

    }

    public static void showTip(Context context, int imgRes, CharSequence title, CharSequence body) {
        showTip(context, defaultTipId, imgRes, title, body);
    }

    public static void showTip(Context context, int tipId, int imgRes, CharSequence title, CharSequence body) {
        showTip(context, defaultTipId, imgRes, title, body, null);
    }

    public static void showTip(Context context, int tipId, int imgRes, CharSequence title, CharSequence body, Intent resultIntent) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(imgRes)
                .setContentTitle(title)
                .setContentText(body);

        if (resultIntent != null) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ResultActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(tipId, mBuilder.build());
    }


    public void ShowCustomView(Context context) {
//        String tittle = "title";
//        String subject = "subject content...";
//        String body = "messbox content aslfiaweflasd f  asdfasdf \n" +
//                "new line .abasdf \n" +
//                "new line 2 \n" +
//                "new line 3 \n" +
//                "new line 4 \n";
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(context);
//        PendingIntent pending = PendingIntent.getActivity(context, 1, new Intent(), 0);
//
//        builder.setContentIntent(pending).setSmallIcon(R.mipmap.ic_launcher)
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(false) //为true的话，一点就消失
//                .setContentTitle(tittle)
//                .setContentText(body);
//        Notification notification = builder.build();
//
//        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.noti_normal_view);
//
//        contentView.setImageViewResource(R.id.img_noti, R.drawable.noti_normal_view);
//
//        contentView.setTextViewText(R.id.txt_noti_title, tittle);
//        contentView.setTextViewTextSize(R.id.txt_noti_title, TypedValue.COMPLEX_UNIT_SP, 16.0F);
//
//        contentView.setTextViewText(R.id.txt_noti_body, body);
//        contentView.setTextViewTextSize(R.id.txt_noti_body, TypedValue.COMPLEX_UNIT_SP, 12.0F);
//
//
//        notification.contentView = (RemoteViews) contentView;
//        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_SHOW_LIGHTS;
//
//
//        notificationManager.notify(0, notification);
    }


}

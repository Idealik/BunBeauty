package com.example.ideal.myapplication.notifications;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.ideal.myapplication.R;

public class NotificationReview extends NotificationConstructor {

    private Context context;
    private static final String CHANNEL_ID = "NotificationReview";

    public NotificationReview(Resources resources, Context context) {
        this.context = context;
    }

    @Override
    public void createNotification() {
        //нужен, чтобы потом обратиться к нему и если что изменить, в нашем случае вроде как не нужен
        int notificationId = 1;

        //создание notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bun_beauty)
                .setContentTitle("Поставьте оценку!")
                .setContentText("Вы можете поставить оценку ИМЯ ПОЛЬЗОВАТЕЛЯ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

}

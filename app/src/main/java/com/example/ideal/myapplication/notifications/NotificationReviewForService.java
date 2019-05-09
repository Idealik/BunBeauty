/*package com.example.ideal.myapplication.notifications;

public class NotificationReviewForService {
}*/
package com.example.ideal.myapplication.notifications;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.ideal.myapplication.R;

public class NotificationReviewForService extends NotificationConstructor {

    private Context context;
    private static final String CHANNEL_ID = "1";

    private String servixeName;

    public NotificationReviewForService(Context _context, String _servixeName) {
        context = _context;
        servixeName = _servixeName;
    }

    @Override
    public void createNotification() {
        //нужен, чтобы потом обратиться к нему и если что изменить, в нашем случае вроде как не нужен
        int notificationId = 1;

        //создание notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bun_beauty)
                .setContentTitle("Возможность оценить")
                .setContentText("У вас есть возможность оценить услугу \"" + servixeName + "\"")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

}

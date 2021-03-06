package com.bunbeauty.ideal.myapplication.clean_architecture.domain.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.ideal.myapplication.R
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.api.firstCapitalSymbol
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.profile.ProfileActivity

class NotificationSubscribers(
    private val context: Context,
    private val userId: String,
    private val name: String,
    private val surname: String,
    private val photoLink: String
) : NotificationConstructor() {

    override fun createNotification() {
        val notificationId = 3
        val intent = Intent(context, ProfileActivity::class.java).apply {
            putExtra(User.USER_ID, userId)
        }
        val pIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        //создание notification
        val builder =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.bun_beauty_icon)
                .setContentIntent(pIntent)
                .setLargeIcon(getBitmapFromURL(photoLink))
                .setContentTitle("Новый подписчик!")
                .setContentText("На вас подписался ${name.firstCapitalSymbol()} ${surname.firstCapitalSymbol()}")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("На вас подписался ${name.firstCapitalSymbol()} ${surname.firstCapitalSymbol()}")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)
        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val CHANNEL_ID = "1"
    }

}
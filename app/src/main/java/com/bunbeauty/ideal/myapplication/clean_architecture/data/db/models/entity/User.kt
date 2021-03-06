package com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey
    override var id: String="",
    var name: String = "",
    var surname: String = "",
    var city: String = "",
    var phone: String = "",
    var rating: Float = 0f,
    var countOfRates: Long = 0,
    var photoLink: String = DEFAULT_PHOTO_LINK,
    var subscribersCount: Long = 0,
    var subscriptionsCount: Long = 0,
    var registrationDate: Long = 0
) : Serializable,BaseModel() {
//добавить время последнего захода?
//добавить дату регистрации?

    companion object {

        var cacheUser = User()

        fun getMyId(): String {
            val curUser = FirebaseAuth.getInstance().currentUser
            if (curUser != null) {
                return curUser.uid
            }
            return ""
        }

        const val USERS = "users"
        const val USER = "user"
        const val USER_ID = "user id"
        const val PHONE = "phone"
        const val NAME = "name"
        const val SURNAME = "surname"
        const val CITY = "city"
        const val PHOTO_LINK = "photo link"
        const val AVG_RATING = "avg rating"
        const val COUNT_OF_RATES = "count of rates"
        const val COUNT_OF_SUBSCRIBERS = "count of subscribers"
        const val COUNT_OF_SUBSCRIPTIONS = "count of subscriptions"
        const val DEFAULT_PHOTO_LINK = "https://firebasestorage." +
                "googleapis.com/v0/b/bun-beauty.appspot.com/o/avatar%2FdefaultAva." +
                "jpg?alt=media&token=f15dbe15-0541-46cc-8272-2578627ed311"

        const val STATUS = "status"
        const val USER_PHOTO = "user photo"
        const val MASTER = "master"
        const val CLIENT = "client"
        const val TOKEN = "token"
        const val REGISTRATION_DATE = "registration date"
    }
}

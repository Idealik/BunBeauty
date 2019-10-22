package com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Subscriber(@PrimaryKey
                 val id: String = "",
                 val userId: String = "",
                 val workerId: String = "") {
    companion object {
        const val SUBSCRIBERS = "subscribers"
        const val USER_ID = "user id"
        const val WORKER_ID = "worker id"
    }
}
package com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.db.DBHelper
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.User
import com.bunbeauty.ideal.myapplication.cleanArchitecture.repositories.logIn.RegistrationRepository

class RegistrationLocalDatabase(context: Context) : RegistrationRepository {

    private val TAG = "data_layer"

    private var dbHelper: DBHelper = DBHelper(context)
    //TODO LOGS

    override fun addUser(user: User) {
        Log.d(TAG, "addUser in local storage $user")

        addUserInLocalStorage(user)
        addPhotoInLocalStorage(user)
    }

    private fun addUserInLocalStorage(user: User) {

        dbHelper.writableDatabase.delete(DBHelper.TABLE_CONTACTS_USERS, null, null)

        val contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_ID, user.id)
        contentValues.put(DBHelper.KEY_NAME_USERS, user.name)
        contentValues.put(DBHelper.KEY_RATING_USERS, "0")
        contentValues.put(DBHelper.KEY_CITY_USERS, user.city)
        contentValues.put(DBHelper.KEY_PHONE_USERS, user.phone)
        contentValues.put(DBHelper.KEY_SUBSCRIBERS_COUNT_USERS, "0")
        contentValues.put(DBHelper.KEY_SUBSCRIPTIONS_COUNT_USERS, "0")

        dbHelper.writableDatabase.insert(DBHelper.TABLE_CONTACTS_USERS, null, contentValues)
        Log.d(TAG, "addUser in local storage completed")
    }

    private fun addPhotoInLocalStorage(user: User) {
        Log.d(TAG, "add photo in local storage ")

        val contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_ID, user.id)
        contentValues.put(DBHelper.KEY_PHOTO_LINK_PHOTOS, user.photoLink)
        contentValues.put(DBHelper.KEY_OWNER_ID_PHOTOS, user.id)

        dbHelper.writableDatabase.insert(DBHelper.TABLE_PHOTOS, null, contentValues)
        Log.d(TAG, "add photo in local storage completed")
    }
}
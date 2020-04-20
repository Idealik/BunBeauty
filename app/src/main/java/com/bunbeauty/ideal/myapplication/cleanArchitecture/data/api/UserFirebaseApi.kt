package com.bunbeauty.ideal.myapplication.cleanArchitecture.data.api

import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.subscribers.user.IUserCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.subscribers.user.IUsersCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class UserFirebaseApi {

    private val TAG = "data_layer"

    fun delete(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun update(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun get(): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insert(user: User) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(User.USERS).child(user.id)

        val items = HashMap<String, Any>()
        items[User.PHONE] = user.phone
        items[User.NAME] = user.name
        items[User.CITY] = user.city
        items[User.AVG_RATING] = user.rating
        items[User.COUNT_OF_RATES] = user.countOfRates
        items[User.PHOTO_LINK] = user.photoLink
        items[User.COUNT_OF_SUBSCRIBERS] = user.subscribersCount
        items[User.COUNT_OF_SUBSCRIPTIONS] = user.subscriptionsCount
        myRef.updateChildren(items)
    }

    fun getById(id: String, callback: IUserCallback) {

        val userRef = FirebaseDatabase.getInstance()
            .getReference(User.USERS)
            .child(id)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val user = getUserFromSnapshot(usersSnapshot)
                callback.returnUser(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByPhoneNumber(phoneNumber: String, callback: IUserCallback) {
        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.PHONE)
            .equalTo(phoneNumber)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                var user = User()
                if (usersSnapshot.childrenCount > 0L) {
                    user = getUserFromSnapshot(usersSnapshot.children.iterator().next())
                }
                callback.returnUser(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByCity(city: String, iUsersCallback: IUsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.CITY)
            .equalTo(city)

        userQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val users = arrayListOf<User>()
                if (usersSnapshot.childrenCount > 0L) {
                    for (userSnapshot in usersSnapshot.children) {
                        users.add(getUserFromSnapshot(userSnapshot))
                    }
                }
                iUsersCallback.returnUsers(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByCityAndUserName(city: String, userName: String, iUsersCallback: IUsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.CITY)
            .equalTo(city)

        userQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val users = arrayListOf<User>()
                if (usersSnapshot.childrenCount > 0L) {
                    for (userSnapshot in usersSnapshot.children) {
                        if (userName == userSnapshot.child(User.NAME).value as? String ?: "")
                            users.add(getUserFromSnapshot(userSnapshot))
                    }
                }
                iUsersCallback.returnUsers(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByName(name: String, iUsersCallback: IUsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.NAME)
            .equalTo(name)

        userQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                if (usersSnapshot.childrenCount > 0L) {
                    val users = arrayListOf<User>()
                    for (userSnapshot in usersSnapshot.children) {
                        users.add(getUserFromSnapshot(userSnapshot))
                    }
                    iUsersCallback.returnUsers(users)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    private fun getUserFromSnapshot(userSnapshot: DataSnapshot): User {
        val user = User()
        // add defualt value
        user.id = userSnapshot.key!!
        user.name = userSnapshot.child(User.NAME).value as? String ?: ""
        user.city = userSnapshot.child(User.CITY).value as? String ?: ""
        user.phone = userSnapshot.child(User.PHONE).value as? String ?: ""
        user.photoLink = userSnapshot.child(User.PHOTO_LINK).value as? String ?: ""
        user.countOfRates = userSnapshot.child(User.COUNT_OF_RATES).getValue(Long::class.java)
            ?: 0L
        user.rating = userSnapshot.child(User.AVG_RATING).getValue(Float::class.java) ?: 0f
        user.subscriptionsCount =
            userSnapshot.child(User.COUNT_OF_SUBSCRIPTIONS).getValue(Long::class.java)
                ?: 0L
        user.subscribersCount =
            userSnapshot.child(User.COUNT_OF_SUBSCRIBERS).getValue(Long::class.java)
                ?: 0L

        return user
    }

    //лучше использовать лист?
    fun filterByUserName(users: ArrayList<User>, userName: String): ArrayList<User> {
        for (user in users) {
            if (user.name != userName) {
                users.remove(user)
            }
        }
        return users
    }
}

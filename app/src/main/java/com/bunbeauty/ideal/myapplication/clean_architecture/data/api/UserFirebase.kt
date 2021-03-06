package com.bunbeauty.ideal.myapplication.clean_architecture.data.api

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.user.UserCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.user.UsersCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.google.firebase.database.*
import java.util.*

class UserFirebase {

    private val TAG = "data_layer"

    fun insert(user: User) {
        val myRef = FirebaseDatabase.getInstance().getReference(User.USERS).child(user.id)

        val items = HashMap<String, Any>()
        items[User.PHONE] = user.phone
        items[User.NAME] = user.name
        items[User.SURNAME] = user.surname
        items[User.CITY] = user.city
        items[User.AVG_RATING] = user.rating
        items[User.COUNT_OF_RATES] = user.countOfRates
        items[User.PHOTO_LINK] = user.photoLink
        items[User.COUNT_OF_SUBSCRIBERS] = user.subscribersCount
        items[User.COUNT_OF_SUBSCRIPTIONS] = user.subscriptionsCount
        items[User.REGISTRATION_DATE] = ServerValue.TIMESTAMP
        myRef.updateChildren(items)
    }

    fun delete(user: User) {

    }

    fun setToken(token: String){
        val reference = FirebaseDatabase.getInstance().reference
        reference.child(User.USERS)
            .child(User.getMyId())
            .child(User.TOKEN)
            .setValue(token)
    }

    fun update(user: User) {
        val myRef = FirebaseDatabase.getInstance().getReference(User.USERS).child(user.id)

        val items = HashMap<String, Any>()
        items[User.PHONE] = user.phone
        items[User.NAME] = user.name
        items[User.SURNAME] = user.surname
        items[User.CITY] = user.city
        items[User.AVG_RATING] = user.rating
        items[User.COUNT_OF_RATES] = user.countOfRates
        items[User.PHOTO_LINK] = user.photoLink
        items[User.COUNT_OF_SUBSCRIBERS] = user.subscribersCount
        items[User.COUNT_OF_SUBSCRIPTIONS] = user.subscriptionsCount
        myRef.updateChildren(items)
    }

    fun get(): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getById(userId: String, userCallback: UserCallback) {
        val userRef = FirebaseDatabase.getInstance()
            .getReference(User.USERS)
            .child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                userCallback.returnGottenObject(getUserFromSnapshot(usersSnapshot))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByPhoneNumber(phoneNumber: String, userCallback: UserCallback) {
        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.PHONE)
            .equalTo(phoneNumber)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                if (usersSnapshot.childrenCount != 0L) {
                    userCallback.returnGottenObject(
                        getUserFromSnapshot(
                            usersSnapshot.children.iterator().next()
                        )
                    )
                } else {
                    userCallback.returnGottenObject(User())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByCity(city: String, usersCallback: UsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.CITY)
            .equalTo(city)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val users = arrayListOf<User>()
                if (usersSnapshot.childrenCount > 0L) {
                    for (userSnapshot in usersSnapshot.children) {
                        users.add(getUserFromSnapshot(userSnapshot))
                    }
                }
                usersCallback.returnList(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByCityAndUserName(city: String, userName: String, usersCallback: UsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.CITY)
            .equalTo(city)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                val users = arrayListOf<User>()
                if (usersSnapshot.childrenCount > 0L) {
                    for (userSnapshot in usersSnapshot.children) {
                        if (userName == userSnapshot.child(User.NAME).value as? String ?: "")
                            users.add(getUserFromSnapshot(userSnapshot))
                    }
                }
                usersCallback.returnList(users)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    fun getByName(name: String, usersCallback: UsersCallback) {

        val userQuery = FirebaseDatabase.getInstance().getReference(User.USERS)
            .orderByChild(User.NAME)
            .equalTo(name)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                if (usersSnapshot.childrenCount > 0L) {
                    val users = arrayListOf<User>()
                    for (userSnapshot in usersSnapshot.children) {
                        users.add(getUserFromSnapshot(userSnapshot))
                    }
                    usersCallback.returnList(users)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Some error
            }
        })
    }

    private fun getUserFromSnapshot(userSnapshot: DataSnapshot): User {
        val user = User()
        user.id = userSnapshot.key!!
        user.name = userSnapshot.child(User.NAME).value as? String ?: ""
        user.surname = userSnapshot.child(User.SURNAME).value as? String ?: ""
        user.city = userSnapshot.child(User.CITY).value as? String ?: ""
        user.phone = userSnapshot.child(User.PHONE).value as? String ?: ""
        user.photoLink = userSnapshot.child(User.PHOTO_LINK).value as? String ?: "1"
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

}

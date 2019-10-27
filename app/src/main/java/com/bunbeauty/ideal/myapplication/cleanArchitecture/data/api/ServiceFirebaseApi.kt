package com.bunbeauty.ideal.myapplication.cleanArchitecture.data.api

import android.util.Log
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.IServiceSubscriber
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.Service
import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ServiceFirebaseApi{

    private val TAG = "data_layer"

    fun insert(service: Service) {
        val database = FirebaseDatabase.getInstance()
        val serviceRef = database
                .getReference(User.USERS)
                .child(service.userId)
                .child(Service.SERVICES)
                .child(service.id)

        val items = HashMap<String, Any>()
        items[Service.NAME] = service.name
        items[Service.ADDRESS] = service.address
        items[Service.DESCRIPTION] = service.description
        items[Service.COST] = service.cost
        items[Service.CATEGORY] = service.category
        items[Service.CREATION_DATE] = service.creationDate
        items[Service.PREMIUM_DATE] = service.premiumDate
        items[Service.AVG_RATING] = service.rating
        items[Service.COUNT_OF_RATES] = service.countOfRates
        serviceRef.updateChildren(items)

        Log.d(TAG, "Service adding completed")
    }

    fun delete(service: Service) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun update(service: Service) {
        val database = FirebaseDatabase.getInstance()
        val serviceRef = database
                .getReference(User.USERS)
                .child(service.userId)
                .child(Service.SERVICES)
                .child(service.id)

        val items = HashMap<String, Any>()
        items[Service.NAME] = service.name
        items[Service.ADDRESS] = service.address
        items[Service.DESCRIPTION] = service.description
        items[Service.COST] = service.cost
        items[Service.CATEGORY] = service.category
        items[Service.CREATION_DATE] = service.creationDate
        items[Service.PREMIUM_DATE] = service.premiumDate
        items[Service.AVG_RATING] = service.rating
        items[Service.COUNT_OF_RATES] = service.countOfRates
        serviceRef.updateChildren(items)
    }

    fun get(): List<Service> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getAllUserServices(userId: String, serviceSubscriber: IServiceSubscriber): List<Service> {
        //val serviceList: ArrayList<Service> = ArrayList()
        val database = FirebaseDatabase.getInstance()
        val servicesRef = database
                .getReference(User.USERS)
                .child(userId)
                .child(Service.SERVICES)

        servicesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(servicesSnapshot: DataSnapshot) {

                val serviceList: ArrayList<Service> = ArrayList()
                for (serviceSnapshot in servicesSnapshot.children) {
                    val service = Service()

                    service.id = serviceSnapshot.key!!
                    service.userId = userId
                    service.name = serviceSnapshot.child(Service.NAME).getValue<String>(String::class.java)!!
                    service.address = serviceSnapshot.child(Service.ADDRESS).getValue<String>(String::class.java)!!
                    service.description = serviceSnapshot.child(Service.DESCRIPTION).getValue<String>(String::class.java)!!
                    service.cost = serviceSnapshot.child(Service.COST).getValue<String>(String::class.java)!!
                    service.countOfRates = serviceSnapshot.child(Service.COUNT_OF_RATES).getValue<Long>(Long::class.java)!!
                    service.rating = serviceSnapshot.child(Service.AVG_RATING).getValue<Float>(Float::class.java)!!
                    service.category = serviceSnapshot.child(Service.CATEGORY).getValue<String>(String::class.java)!!
                    service.creationDate = serviceSnapshot.child(Service.CREATION_DATE).getValue<String>(String::class.java)!!
                    service.premiumDate = serviceSnapshot.child(Service.PREMIUM_DATE).getValue<String>(String::class.java)!!

                    serviceList.add(service)
                }

                serviceSubscriber.returnServiceList(serviceList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Some error
            }
        })

        return ArrayList()
    }

    fun getIdForNew(userId: String): String{
        return FirebaseDatabase.getInstance().getReference(User.USERS)
                .child(userId)
                .child(Service.SERVICES).push().key!!
    }
}

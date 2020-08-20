package com.bunbeauty.ideal.myapplication.clean_architecture.business.fragments

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.ISearchServiceCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.user.UserCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class SearchServiceInteractor(val userRepository: UserRepository) : UserCallback {

    lateinit var searchServiceCallback: ISearchServiceCallback
    var cities = arrayListOf<String>()

    fun setMyCity(cities: ArrayList<String>, searchServiceCallback: ISearchServiceCallback) {
        this.searchServiceCallback = searchServiceCallback
        this.cities = cities
        userRepository.getById(getUserId(), this, false)
    }

    fun getUserId(): String = FirebaseAuth.getInstance().currentUser!!.uid


    override fun returnGottenObject(element: User?) {
        if (element == null) return

    }
}
package com.bunbeauty.ideal.myapplication.cleanArchitecture.callback

import com.bunbeauty.ideal.myapplication.cleanArchitecture.models.entity.User

interface AuthorizationCallback {
    fun goToRegistration()
    fun goToProfile()
}
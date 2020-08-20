package com.bunbeauty.ideal.myapplication.clean_architecture.mvp.views.log_in

import com.arellomobile.mvp.MvpView
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User

interface RegistrationView : MvpView {
    fun setNameInputError(error: String)
    fun setSurnameInputError(error: String)
    fun showNoSelectedCity()
    fun goToProfile(user: User)
    fun fillPhoneInput(phone: String)
    fun showSuccessfulRegistration()
    fun disableRegistrationButton()
    fun enableRegistrationButton()
}
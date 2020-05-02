package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.logIn

import com.arellomobile.mvp.MvpView

interface RegistrationView: MvpView {
    fun setNameInputError(error:String)
    fun setSurnameInputError(error:String)
    fun showNoSelectedCity()
    fun goToProfile()
    fun fillPhoneInput(phone: String)
    fun showSuccessfulRegistration()
    fun disableRegistrationButton()
    fun enableRegistrationButton()
}
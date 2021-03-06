package com.bunbeauty.ideal.myapplication.clean_architecture.domain.log_in

import android.content.Intent
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.log_in.iLogIn.IRegistrationUserInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.logIn.RegistrationPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.user.InsertUsersCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.interface_repositories.IUserRepository
import com.google.firebase.auth.FirebaseAuth

class RegistrationUserInteractor(
    private val userRepository: IUserRepository) : IRegistrationUserInteractor, InsertUsersCallback {

    private lateinit var registrationPresenterCallback: RegistrationPresenterCallback

    override fun getMyPhoneNumber(intent: Intent): String = intent.getStringExtra(User.PHONE) ?: ""

    override fun registerUser(
        user: User,
        registrationPresenterCallback: RegistrationPresenterCallback
    ) {
        this.registrationPresenterCallback = registrationPresenterCallback
        if (isNameCorrect(user.name, registrationPresenterCallback)
            && isSurnameCorrect(user.surname, registrationPresenterCallback)
            && isCityCorrect(user.city, registrationPresenterCallback)
        ) {
            user.id = getUserId()
            userRepository.insert(user, this)
        }
    }

    private fun getUserId(): String = FirebaseAuth.getInstance().currentUser!!.uid

    //TODO UNIT TEST
    private fun isNameCorrect(
        name: String,
        registrationPresenterCallback: RegistrationPresenterCallback
    ): Boolean {
        if (name.isEmpty()) {
            registrationPresenterCallback.registrationNameInputErrorEmpty()
            return false
        }

        if (!getIsNameInputCorrect(name)) {
            registrationPresenterCallback.registrationNameInputError()
            return false
        }

        if (!getIsNameLengthLessTwenty(name)) {
            registrationPresenterCallback.registrationNameInputErrorLong()
            return false
        }
        return true
    }

    private fun isSurnameCorrect(
        surname: String,
        registrationPresenterCallback: RegistrationPresenterCallback
    ): Boolean {
        if (surname.isEmpty()) {
            registrationPresenterCallback.registrationSurnameInputErrorEmpty()
            return false
        }

        if (!getIsNameInputCorrect(surname)) {
            registrationPresenterCallback.registrationSurnameInputError()
            return false
        }

        if (!getIsNameLengthLessTwenty(surname)) {
            registrationPresenterCallback.registrationSurnameInputErrorLong()
            return false
        }
        return true
    }

    private fun isCityCorrect(
        city: String,
        registrationPresenterCallback: RegistrationPresenterCallback
    ): Boolean {
        if (!getIsCityInputCorrect(city)) {
            registrationPresenterCallback.registrationCityInputError()
            return false
        }
        return true
    }

    fun getIsCityInputCorrect(city: String): Boolean {
        return city != "Город"
    }

    fun getIsNameInputCorrect(name: String): Boolean {
        return name.matches("[a-zA-ZА-Яа-я\\-]+".toRegex())
    }

    fun getIsNameLengthLessTwenty(name: String): Boolean {
        return name.length <= 20
    }

    fun getIsSurnameInputCorrect(surname: String): Boolean {
        return surname.matches("[a-zA-ZА-Яа-я\\-]+".toRegex())
    }

    fun getIsSurnameLengthLessTwenty(surname: String): Boolean {
        return surname.length <= 20
    }

    override fun returnCreatedCallback(obj: User) {
        registrationPresenterCallback.showSuccessfulRegistration(obj)
    }

}
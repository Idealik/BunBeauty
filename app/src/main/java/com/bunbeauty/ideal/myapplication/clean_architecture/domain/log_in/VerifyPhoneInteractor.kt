package com.bunbeauty.ideal.myapplication.clean_architecture.domain.log_in

import android.content.Intent
import android.util.Log
import com.bunbeauty.ideal.myapplication.clean_architecture.Tag
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.api.VerifyPhoneNumberApi
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.log_in.iLogIn.IVerifyPhoneInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.VerifyPhoneNumberCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.VerifyPhonePresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.user.UserCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.BaseRepository
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.interface_repositories.IUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential

class VerifyPhoneInteractor(
    private val userRepository: IUserRepository,
    private val verifyPhoneNumberApi: VerifyPhoneNumberApi
) : BaseRepository(), IVerifyPhoneInteractor, UserCallback, VerifyPhoneNumberCallback {

    private lateinit var verifyPresenterCallback: VerifyPhonePresenterCallback
    private var phoneNumber = ""

    override fun getPhoneNumber(intent: Intent): String {
        phoneNumber = intent.getStringExtra(User.PHONE) ?: ""
        return phoneNumber
    }

    override fun sendVerificationCode(
        phoneNumber: String,
        verifyPresenterCallback: VerifyPhonePresenterCallback
    ) {
        this.verifyPresenterCallback = verifyPresenterCallback

        verifyPhoneNumberApi.sendVerificationCode(phoneNumber, this)
    }

    override fun resendVerificationCode(phoneNumber: String) {
        verifyPhoneNumberApi.resendVerificationCode(phoneNumber)
    }

    override fun checkCode(code: String) {
        verifyPhoneNumberApi.checkCode(code, this)
    }

    override fun returnTooManyRequestsError() {
        verifyPresenterCallback.showTooManyRequestsError()
    }

    override fun returnVerificationFailed() {
        verifyPresenterCallback.showVerificationFailed()
    }

    override fun returnTooShortCodeError() {
        verifyPresenterCallback.showTooShortCodeError()
    }

    override fun returnCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userRepository.getByPhoneNumber(phoneNumber, this, true)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    verifyPresenterCallback.showWrongCodeError()
                }

                Log.d(Tag.ERROR_TAG, task.exception.toString())
            }
        }
    }

    override fun returnServiceConnectionProblem() {
        verifyPresenterCallback.showServiceConnectionProblem()
    }

    override fun returnGottenObject(element: User?) {
        if (element == null) return

        if (element.name.isEmpty()) {
            verifyPresenterCallback.goToRegistration(phoneNumber)
        } else {
            verifyPresenterCallback.goToProfile()
        }
    }

    /*override fun returnWrongCodeError() {
        verifyPresenterCallback.showWrongCodeError()
    }

    override fun returnVerifySuccessful(credential: PhoneAuthCredential) {
        userRepository.getByPhoneNumber(getMyPhoneNumber(), this, true)
    }*/


}
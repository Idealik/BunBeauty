package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.logIn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.logIn.VerifyPhoneInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.AppModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.profile.ProfileActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.logIn.VerifyPhonePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.logIn.VerifyPhoneView
import com.bunbeauty.ideal.myapplication.helpApi.WorkWithViewApi
import kotlinx.android.synthetic.main.activity_verify_phone_number.*
import javax.inject.Inject

class VerifyPhoneNumberActivity : MvpAppCompatActivity(), VerifyPhoneView {

    @Inject
    internal lateinit var verifyPhoneInteractor: VerifyPhoneInteractor

    @InjectPresenter
    internal lateinit var verifyPhonePresenter: VerifyPhonePresenter

    @ProvidePresenter
    internal fun provideVerifyPhonePresenter(): VerifyPhonePresenter {
        DaggerAppComponent.builder()
            .appModule(AppModule(application, intent))
            .build()
            .inject(this)

        return VerifyPhonePresenter(verifyPhoneInteractor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_number)

        configViews()
        showViewsOnScreen()

        verifyPhonePresenter.sendCode()
    }

    private fun configViews() {
        phoneVerifyPhoneInput.setText(verifyPhonePresenter.getPhoneNumber())

        verifyVerifyPhoneBtn.setOnClickListener{
            WorkWithViewApi.hideKeyboard(this)
            verifyPhonePresenter.checkCode(codeVerifyPhoneInput.text.toString())
        }
        resendCodeVerifyPhoneText.setOnClickListener{
            WorkWithViewApi.hideKeyboard(this)
            verifyPhonePresenter.resendCode()
        }
        changePhoneVerifyPhoneText.setOnClickListener{
            WorkWithViewApi.hideKeyboard(this)
            goBackToAuthorization()
        }
    }

    override fun hideViewsOnScreen() {
        verifyVerifyPhoneBtn.visibility = View.GONE
        loadingVerifyPhoneProgressBar.visibility = View.VISIBLE
    }

    override fun showViewsOnScreen() {
        verifyVerifyPhoneBtn.visibility = View.VISIBLE
        loadingVerifyPhoneProgressBar.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showSendCode() {

    }

    private fun goBackToAuthorization() {
        super.onBackPressed()
    }

    override fun goToRegistration(phone: String) {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.putExtra(User.PHONE, phone)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun goToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}
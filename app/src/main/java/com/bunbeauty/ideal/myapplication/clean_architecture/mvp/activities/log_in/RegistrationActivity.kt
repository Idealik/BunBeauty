package com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.log_in

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.clean_architecture.business.WorkWithStringsApi
import com.bunbeauty.ideal.myapplication.clean_architecture.business.log_in.iLogIn.IRegistrationUserInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.di.AppModule
import com.bunbeauty.ideal.myapplication.clean_architecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.clean_architecture.di.FirebaseModule
import com.bunbeauty.ideal.myapplication.clean_architecture.di.InteractorModule
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.profile.ProfileActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.intarfaces.IAdapterSpinner
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.presenters.log_in.RegistrationPresenter
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.views.log_in.RegistrationView
import com.bunbeauty.ideal.myapplication.help_api.WorkWithViewApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

class RegistrationActivity : MvpAppCompatActivity(), RegistrationView, IAdapterSpinner {

    @Inject
    lateinit var registrationUserInteractor: IRegistrationUserInteractor

    @InjectPresenter
    lateinit var registrationPresenter: RegistrationPresenter

    @ProvidePresenter
    internal fun provideRegistrationPresenter(): RegistrationPresenter {
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .firebaseModule(FirebaseModule())
            .interactorModule(InteractorModule(intent))
            .build()
            .inject(this)

        return RegistrationPresenter(registrationUserInteractor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        configViews()
    }

    private fun configViews() {
        phoneRegistrationInput.setText(registrationPresenter.getMyPhoneNumber())

        setAdapter(
            arrayListOf(*resources.getStringArray(R.array.cities)),
            cityRegistrationSpinner,
            this
        )
        (cityRegistrationSpinner.adapter as ArrayAdapter<String>).filter.filter("")

        saveRegistrationBtn.setOnClickListener {
            WorkWithViewApi.hideKeyboard(this)
            registrationPresenter.registerUser(
                WorkWithStringsApi.firstCapitalSymbol(nameRegistrationInput.text.toString().trim()),
                WorkWithStringsApi.firstCapitalSymbol(
                    surnameRegistrationInput.text.toString().trim()
                ),
                WorkWithStringsApi.firstCapitalSymbol(cityRegistrationSpinner.text.toString()),
                phoneRegistrationInput.text.toString()
            )
        }

    }

    override fun fillPhoneInput(phone: String) = phoneRegistrationInput.setText(phone)

    override fun disableRegistrationButton() {
        saveRegistrationBtn.isEnabled = false
    }

    override fun enableRegistrationButton() {
        saveRegistrationBtn.isEnabled = true
    }

    override fun setNameInputError(error: String) {
        nameRegistrationInput.error = error
        nameRegistrationInput.requestFocus()
    }

    override fun setSurnameInputError(error: String) {
        surnameRegistrationInput.error = error
        surnameRegistrationInput.requestFocus()
    }

    override fun showNoSelectedCity() {
        Snackbar.make(registrationLinearLayout, "Выберите город", Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.red))
            .setActionTextColor(ContextCompat.getColor(this, R.color.white)).show()
    }

    override fun showSuccessfulRegistration() {
        Snackbar.make(registrationLinearLayout, "Пользователь зарегестирован", Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.mainBlue))
            .setActionTextColor(ContextCompat.getColor(this, R.color.white)).show()
    }

    override fun goToProfile(user: User) {
        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra(User.USER, user)
            putExtra(REGISTRATION_ACTIVITY, "registration")
        }
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    companion object{
        const val REGISTRATION_ACTIVITY = "registration activity"
    }
}
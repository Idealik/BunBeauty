package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.fragments.PremiumElementInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.AppModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Service
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.fragments.PremiumElementPresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.fragments.PremiumElementFragmentView
import javax.inject.Inject

class PremiumElementFragment @SuppressLint("ValidFragment")
constructor() : MvpAppCompatFragment(), View.OnClickListener, PremiumElementFragmentView {

    lateinit var service:Service
    private lateinit var premiumText: TextView
    private lateinit var noPremiumText: TextView
    private lateinit var bottomLayout:LinearLayout

    constructor(service:Service) : this(){
        this.service = service
    }

    private lateinit var code: String
    private lateinit var codeText: TextView

    @InjectPresenter
    lateinit var premiumElementPresenter: PremiumElementPresenter

    @Inject
    lateinit var premiumElementInteractor: PremiumElementInteractor

    @ProvidePresenter
    internal fun provideElementPresenter(): PremiumElementPresenter {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(activity!!.application, activity!!.intent))
                .build().inject(this)

        return PremiumElementPresenter(premiumElementInteractor)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.premium_element, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.setPremiumPremiumElementBtn).setOnClickListener(this)
        view.findViewById<Button>(R.id.continuePremiumElementBtn).setOnClickListener(this)
        codeText = view.findViewById(R.id.codePremiumElement)
        premiumText = view.findViewById(R.id.yesPremiumPremiumElementText)
        noPremiumText = view.findViewById(R.id.noPremiumPremiumElementText)
        bottomLayout = view.findViewById(R.id.premiumAddServiceBottomLayout)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.setPremiumPremiumElementBtn -> {
                code = codeText.text.toString().toLowerCase().trim { it <= ' ' }
                premiumElementPresenter.setPremium(code, service)
            }
            R.id.continuePremiumElementBtn -> {
                //go to
            }
        }
    }

    companion object {
        private val TAG = "DBInf"
    }

    override fun showError(error: String) {
        codeText.error = error
        codeText.requestFocus()
    }

    override fun showPremiumActivated() {
        Toast.makeText(context, "Премиум активирован", Toast.LENGTH_LONG).show()
    }

    override fun setWithPremium() {
        noPremiumText.visibility = View.GONE
        premiumText.visibility = View.VISIBLE
        premiumText.isEnabled = false
    }
    override fun hideBottom() {
        bottomLayout.visibility = View.GONE
    }

}
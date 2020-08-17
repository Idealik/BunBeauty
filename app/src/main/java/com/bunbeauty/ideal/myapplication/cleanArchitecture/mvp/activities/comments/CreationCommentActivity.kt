package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments

import android.content.Intent
import android.os.Bundle
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.commets.creationComment.*
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Message
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.AppModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.FirebaseModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.InteractorModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.comments.CreationCommentPresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.comments.CreationCommentView
import kotlinx.android.synthetic.main.activity_creation_comment.*
import javax.inject.Inject

class CreationCommentActivity : MvpAppCompatActivity(), CreationCommentView {

    @Inject
    lateinit var creationCommentUserCommentInteractor: CreationCommentUserCommentInteractor

    @Inject
    lateinit var creationCommentMessageInteractor: CreationCommentMessageInteractor

    @Inject
    lateinit var creationCommentServiceCommentInteractor: CreationCommentServiceCommentInteractor

    @Inject
    lateinit var creationCommentOrderInteractor: CreationCommentOrderInteractor

    @Inject
    lateinit var creationCommentUserInteractor: CreationCommentUserInteractor

    @InjectPresenter
    lateinit var creationCommentPresenter: CreationCommentPresenter

    @ProvidePresenter
    internal fun provideProfilePresenter(): CreationCommentPresenter {
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .firebaseModule(FirebaseModule())
            .interactorModule(InteractorModule(intent))
            .build()
            .inject(this)
        return CreationCommentPresenter(
            creationCommentUserCommentInteractor,
            creationCommentServiceCommentInteractor,
            creationCommentOrderInteractor,
            creationCommentMessageInteractor,
            creationCommentUserInteractor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_comment)
        configViews()
    }

    private fun configViews() {
        rateCreationCommentBtn.setOnClickListener {
            creationCommentPresenter.checkMessage(
                ratingCreationCommentRatingBar.rating,
                reviewCreationCommentInput.text.toString()
            )
        }
    }

    override fun showCommentCreated(message: Message, user: User) {
        val intent = Intent()
        intent.putExtra(Message.MESSAGE, message)
        intent.putExtra(User.USER, user)
        setResult(RESULT_OK, intent)
        finish()
    }

}

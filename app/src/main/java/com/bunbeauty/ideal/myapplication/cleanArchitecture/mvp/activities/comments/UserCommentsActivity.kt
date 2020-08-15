package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.adapters.UserCommentAdapter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.commets.userComments.UserCommentsUserCommentInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.commets.userComments.UserCommentsUserInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.comment.UserComment
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.AppModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.FirebaseModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.InteractorModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.enums.ButtonTask
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.interfaces.IBottomPanel
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.interfaces.ITopPanel
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.comments.UserCommentsPresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.comments.UserCommentsView
import kotlinx.android.synthetic.main.activity_comments.*
import javax.inject.Inject

class UserCommentsActivity : MvpAppCompatActivity(), UserCommentsView, ITopPanel, IBottomPanel {

    override var panelContext: Activity = this

    @Inject
    lateinit var userCommentAdapter: UserCommentAdapter

    @Inject
    lateinit var userCommentsCommentInteractor: UserCommentsUserCommentInteractor

    @Inject
    lateinit var userCommentsUserInteractor: UserCommentsUserInteractor

    @InjectPresenter
    lateinit var userCommentsPresenter: UserCommentsPresenter

    @ProvidePresenter
    internal fun commentsPresenter(): UserCommentsPresenter {
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .firebaseModule(FirebaseModule())
            .interactorModule(InteractorModule(intent))
            .build()
            .inject(this)
        return UserCommentsPresenter(
            userCommentsCommentInteractor,
            userCommentsUserInteractor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        init()
        initTopPanel("Оценки пользователя", ButtonTask.NONE)
        initBottomPanel()
        hideEmptyScreen()

        userCommentsPresenter.createUserCommentsScreen()
    }

    fun init() {
        resultsUserCommentsRecycleView.layoutManager = LinearLayoutManager(this)
        resultsUserCommentsRecycleView.adapter = userCommentAdapter
    }

    override fun showLoading() {
        progressBarUserComments.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBarUserComments.visibility = View.GONE
    }

    override fun updateUserComments(userComments: List<UserComment>) {
        userCommentAdapter.setData(userComments)
    }

    override fun showUserComments() {
        resultsUserCommentsRecycleView.visibility = View.VISIBLE
    }

    override fun hideUserComments() {
        resultsUserCommentsRecycleView.visibility = View.GONE
    }

    override fun showEmptyScreen() {
        noUserCommentsText.visibility = View.VISIBLE
    }

    override fun hideEmptyScreen() {
        noUserCommentsText.visibility = View.GONE
    }

}

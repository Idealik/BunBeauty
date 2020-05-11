package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.chat

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.chat.iChat.IDialogsDialogInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.chat.iChat.IDialogsMessageInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.chat.iChat.IDialogsUserInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.chat.DialogsPresenterCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Message
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.chat.DialogsView

@InjectViewState
class DialogsPresenter(
    private val dialogsDialogInteractor: IDialogsDialogInteractor,
    private val dialogsUserInteractor: IDialogsUserInteractor,
    private val dialogsMessageInteractor: IDialogsMessageInteractor
) :
    MvpPresenter<DialogsView>(), DialogsPresenterCallback {

    fun getDialogs() {
        dialogsDialogInteractor.getDialogs(this)
    }

    fun getDialogsLink() = dialogsDialogInteractor.getDialogsLink()

    override fun getUsers(dialogs: List<Dialog>) {
        dialogsUserInteractor.getUsers(dialogs, this)
    }

    override fun fillDialogs(users: List<User>) {
        dialogsDialogInteractor.fillDialogs(users, this)
    }

    override fun showDialogs(dialogs: List<Dialog>) {
        viewState.hideLoading()
        viewState.hideEmptyDialogs()
        viewState.showDialogs(dialogs)
    }

    override fun getMessages(dialogs: List<Dialog>, companionDialogs: List<Dialog>) {
        dialogsMessageInteractor.getMyMessages(dialogs, companionDialogs, this)
    }

    override fun fillDialogsByMessages(messages: List<Message>) {
        dialogsDialogInteractor.fillDialogsByMessages(messages, this)
    }

    override fun showLoading() {
        viewState.showLoading()
    }

    override fun hideLoading() {
        viewState.hideLoading()
    }

    override fun showEmptyDialogs() {
        viewState.showEmptyDialogs()
    }

    override fun hideEmptyDialogs() {
        viewState.hideEmptyDialogs()
    }

}
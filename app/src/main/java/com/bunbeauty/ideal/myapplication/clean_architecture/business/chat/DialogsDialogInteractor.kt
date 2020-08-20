package com.bunbeauty.ideal.myapplication.clean_architecture.business.chat

import com.bunbeauty.ideal.myapplication.clean_architecture.business.chat.i_chat.IDialogsDialogInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.chat.DialogsPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogChangedCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogsCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Message
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.interface_repositories.IDialogRepository

class DialogsDialogInteractor(private val dialogRepository: IDialogRepository) :
    IDialogsDialogInteractor, DialogCallback, DialogsCallback, DialogChangedCallback {

    private var finalCacheDialogs = mutableListOf<Dialog>()
    private var myCacheDialogs = mutableListOf<Dialog>()
    private lateinit var dialogsPresenterCallback: DialogsPresenterCallback
    private var dialogCount = 0

    override fun getDialogsLink() = finalCacheDialogs

    override fun getDialogs(dialogsPresenterCallback: DialogsPresenterCallback) {
        this.dialogsPresenterCallback = dialogsPresenterCallback
        dialogRepository.getByUserId(User.getMyId(), this, this, this)
    }

    override fun returnList(objects: List<Dialog>) {
        if (objects.isEmpty()) {
            dialogsPresenterCallback.showEmptyDialogs()
            dialogsPresenterCallback.hideLoading()
            return
        }

        myCacheDialogs.addAll(objects)

        for (dialog in objects) {
            dialogsPresenterCallback.getUser(dialog)
        }
    }

    override fun returnGottenObject(element: Dialog?) {
        if (element == null) return

        if (element.ownerId == User.getMyId()) {
            myCacheDialogs.add(element)
            dialogsPresenterCallback.getUser(element)
        }
    }

    override fun fillDialogs(
        user: User,
        dialogsPresenterCallback: DialogsPresenterCallback
    ) {
        val dialogWithUserId = myCacheDialogs.find { it.user.id == user.id }
        if (dialogWithUserId != null) {
            dialogWithUserId.user = user
            //получаю последнее сообщение из диалога
            dialogsPresenterCallback.getLastMessage(User.getMyId(), user.id)
        }
    }

    override fun fillDialogsByMessages(
        message: Message,
        dialogsPresenterCallback: DialogsPresenterCallback
    ) {
        dialogCount++

        val dialogWithMessageId =
            myCacheDialogs.find { it.user.id == message.userId }

        if (dialogWithMessageId != null) {
            dialogWithMessageId.lastMessage = message
        }

        if (dialogCount >= myCacheDialogs.size) {
            finalCacheDialogs.clear()
            finalCacheDialogs.addAll(myCacheDialogs.sortedByDescending { it.lastMessage.time })
            dialogsPresenterCallback.showDialogs(finalCacheDialogs)
        }
    }

    override fun returnChanged(element: Dialog) {
        val dialog = myCacheDialogs.find { it.id == element.id }
        if (dialog != null) {
            dialog.isChecked = element.isChecked
            dialogsPresenterCallback.getLastMessage(element.id, element.user.id)
        }
    }
}
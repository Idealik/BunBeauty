package com.bunbeauty.ideal.myapplication.clean_architecture.business.chat.i_chat

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.chat.MessagesPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Message

interface IMessagesMessageInteractor {
    fun getMyMessages(dialog: Dialog, messagesPresenterCallback: MessagesPresenterCallback)
    fun getMyMessagesLink(): List<Message>
    fun updateMessages(message: Message, messagesPresenterCallback: MessagesPresenterCallback)
    fun sendMessage(message: Message, messagesPresenterCallback: MessagesPresenterCallback)

    fun getId(userId: String, dialogId: String): String
}
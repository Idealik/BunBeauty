package com.bunbeauty.ideal.myapplication.clean_architecture.domain.chat.i_chat.message.i_message

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.chat.MessagesPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Order

interface IMessageScheduleInteractor {
    fun deleteOrderFromSchedule(
        order: Order, messagesPresenterCallback: MessagesPresenterCallback
    )
}
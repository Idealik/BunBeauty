package com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.message

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.base_subscribers.BaseGetCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Message

interface MessageCallback : BaseGetCallback<Message>
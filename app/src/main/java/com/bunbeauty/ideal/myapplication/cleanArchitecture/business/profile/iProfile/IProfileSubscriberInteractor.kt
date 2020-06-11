package com.bunbeauty.ideal.myapplication.cleanArchitecture.business.profile.iProfile

import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.profile.ProfilePresenterCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Subscriber

interface IProfileSubscriberInteractor {
    fun checkSubscriber(
        subscriber: Subscriber,
        profilePresenterCallback: ProfilePresenterCallback
    )

    fun checkSubscribed(
        userId: String,
        subscribers: List<Subscriber>,
        profilePresenterCallback: ProfilePresenterCallback
    )
}
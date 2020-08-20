package com.bunbeauty.ideal.myapplication.clean_architecture.business.profile

import com.bunbeauty.ideal.myapplication.clean_architecture.business.profile.iProfile.IProfileServiceInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.profile.ProfilePresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.service.GetServicesCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Service
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.interface_repositories.IServiceRepository

class ProfileServiceInteractor(private val serviceRepository: IServiceRepository) :
    IProfileServiceInteractor, GetServicesCallback {

    private lateinit var profilePresenterCallback: ProfilePresenterCallback

    override fun getServicesByUserId(
        userId: String,
        profilePresenterCallback: ProfilePresenterCallback
    ) {
        this.profilePresenterCallback = profilePresenterCallback

        serviceRepository.getByUserId(userId, this, true)
    }

    override fun returnList(serviceList: List<Service>) {
        profilePresenterCallback.showServiceList(serviceList)
    }

}
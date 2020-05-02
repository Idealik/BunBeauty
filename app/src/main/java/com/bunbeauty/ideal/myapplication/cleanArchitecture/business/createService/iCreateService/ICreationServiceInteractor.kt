package com.bunbeauty.ideal.myapplication.cleanArchitecture.business.createService.iCreateService

import com.bunbeauty.ideal.myapplication.cleanArchitecture.callback.creationService.CreationServicePresenterCallback
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Service

interface ICreationServiceInteractor {
    fun getIsNameInputCorrect(name: String): Boolean
    fun getIsNameLengthLessTwenty(name: String): Boolean
    fun getIsDescriptionInputCorrect(description: String): Boolean
    fun getIsDescriptionLengthLessTwoHundred(description: String): Boolean
    fun getIsCostInputCorrect(cost: String): Boolean
    fun getIsCostLengthLessTen(cost: String): Boolean
    fun getIsCategoryInputCorrect(city: String): Boolean
    fun getIsAddressInputCorrect(address: String): Boolean
    fun getIsAddressLengthThirty(address: String): Boolean

    fun addService(
        service: Service,
        tags: List<String>,
        fpathOfImages: List<String>,
        creationServicePresenterCallback: CreationServicePresenterCallback
    )

    fun addImages(fpathOfImages: List<String>, service: Service)
}

package com.bunbeauty.ideal.myapplication.clean_architecture.mvp.presenters.log_in

import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.editing.service.IEditServiceServiceInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.editing.service.IEditServiceTagInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.photo.IPhotoCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.photo.IPhotoInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.service.EditServicePresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Photo
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Service
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Tag
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.views.log_in.EditServiceView

@InjectViewState
class EditServicePresenter(
    private val editServiceServiceInteractor: IEditServiceServiceInteractor,
    private val photoInteractor: IPhotoInteractor,
    private val editServiceTagInteractor: IEditServiceTagInteractor,
    private val intent: Intent
) :
    MvpPresenter<EditServiceView>(), EditServicePresenterCallback, IPhotoCallback {

    fun getService() {
        editServiceServiceInteractor.getService(intent, this)
    }

    fun saveService(
        name: String,
        address: String,
        description: String,
        cost: Long,
        durationHour: Int,
        durationMinute: Int,
        category: String,
        tags: ArrayList<Tag>
    ) {
        val service = editServiceServiceInteractor.getGottenService()
        service.name = name
        service.address = address
        service.description = description
        service.cost = cost
        service.duration = durationHour + durationMinute * 0.5f
        service.category = category
        service.tags = tags
        editServiceServiceInteractor.update(service, this)
    }

    fun delete() {
        val service = editServiceServiceInteractor.getGottenService()
        editServiceServiceInteractor.delete(service, this)
    }

    fun getCacheService() = editServiceServiceInteractor.getGottenService()

    fun getPhotosLink() = photoInteractor.getPhotoLinkList()

    override fun showEditService(service: Service) {
        viewState.showEditService(service)
        editServiceTagInteractor.setCachedServiceTags(service.tags)
        photoInteractor.getPhotos(service, this)
    }

    override fun addPhoto(photo: Photo) {
        photoInteractor.addPhoto(photo)
    }

    override fun saveTags(service: Service) {
        editServiceTagInteractor.saveTags(service)
        service.photos.addAll(photoInteractor.getPhotoLinkList())
        photoInteractor.savePhotos(photoInteractor.getPhotoLinkList(), service, this)
        viewState.goToService(service)
    }

    override fun goToProfile(service: Service) {
        photoInteractor.deletePhotosFromStorage(
            Service.SERVICE_PHOTO,
            photoInteractor.getPhotoLinkList()
        )
        viewState.showMessage("Услуга успешно удалена")
        viewState.goToProfile(service)
    }

    override fun showNameInputError(error: String) {
        viewState.showNameInputError(error)
    }

    override fun showDescriptionInputError(error: String) {
        viewState.showDescriptionInputError(error)
    }

    override fun showCostInputError(error: String) {
        viewState.showCostInputError(error)
    }

    override fun showCategoryInputError(error: String) {
        viewState.showError(error)
    }

    override fun showAddressInputError(error: String) {
        viewState.showAddressInputError(error)
    }

    override fun showDurationInputError(error: String) {
        viewState.showError(error)
    }

    override fun returnPhotos(photos: List<Photo>) {
        viewState.updatePhotoFeed()
        viewState.hideLoading()
    }

    override fun returnCreatedPhotoLink(uri: Uri) {}

    fun removePhoto(photo: Photo) {
        photoInteractor.removePhoto(photo)
        viewState.updatePhotoFeed()
    }

    fun createPhoto(resultUri: Uri) {
        val photo = Photo()
        photo.link = resultUri.toString()
        photoInteractor.addPhoto(photo)
        viewState.updatePhotoFeed()
    }
}
package com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.createService

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.ideal.myapplication.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.adapters.ChangeablePhotoAdapter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.adapters.elements.CategoryFragment
import com.bunbeauty.ideal.myapplication.cleanArchitecture.adapters.elements.photoElement.IPhotoElement
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.photo.PhotoInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.createService.CreationServiceServiceServiceInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.business.createService.CreationServiceTagInteractor
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Photo
import com.bunbeauty.ideal.myapplication.cleanArchitecture.data.db.models.entity.Service
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.AppModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.DaggerAppComponent
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.FirebaseModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.di.InteractorModule
import com.bunbeauty.ideal.myapplication.cleanArchitecture.enums.ButtonTask
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.PhotoDialogActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.ScheduleActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.fragments.PremiumFragment
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.interfaces.IBottomPanel
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.interfaces.ITopPanel
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.presenters.CreationServicePresenter
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.views.AddingServiceView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_creation_service.*
import javax.inject.Inject

class CreationServiceActivity : MvpAppCompatActivity(), AddingServiceView,
    IBottomPanel, ITopPanel, IPhotoElement {

    override var panelContext: Activity = this
    private lateinit var changeablePhotoAdapter: ChangeablePhotoAdapter

    private lateinit var categoryFragment: CategoryFragment

    @InjectPresenter
    lateinit var creationServicePresenter: CreationServicePresenter

    @Inject
    lateinit var creationServiceServiceInteractor: CreationServiceServiceServiceInteractor

    @Inject
    lateinit var creationServiceTagInteractor: CreationServiceTagInteractor

    @Inject
    lateinit var photoInteractor: PhotoInteractor

    @ProvidePresenter
    internal fun provideAddingServicePresenter(): CreationServicePresenter {
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .firebaseModule(FirebaseModule())
            .interactorModule(InteractorModule(intent))
            .build()
            .inject(this)

        return CreationServicePresenter(
            creationServiceServiceInteractor,
            creationServiceTagInteractor,
            photoInteractor
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_service)
        init()
        showMainBlock()
    }

    override fun onResume() {
        super.onResume()
        initBottomPanel()
    }

    private fun init() {
        resultsCreationServiceRecycleView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        changeablePhotoAdapter = ChangeablePhotoAdapter(
            creationServicePresenter.getPhotosLink(), this,
            resources.getDimensionPixelSize(R.dimen.photo_width),
            resources.getDimensionPixelSize(R.dimen.photo_height)
        )
        resultsCreationServiceRecycleView.adapter = changeablePhotoAdapter

        addServiceCreationServiceBtn.setOnClickListener {
            creationServicePresenter.addService(
                nameCreationServiceInput.text.toString(),
                descriptionCreationServiceInput.text.toString(),
                costCreationServiceInput.text.toString().toLongOrNull() ?: 0,
                addressCreationServiceInput.text.toString(),
                categoryFragment.getCategory(),
                categoryFragment.getSelectedTags()
            )
        }
        photoCreationServiceBtn.setOnClickListener {
            CropImage.activity().start(this)
        }
        continueCreationServiceBtn.setOnClickListener {
            goToSchedule()
        }

        initTopPanel("Создание услуги", ButtonTask.NONE)
        categoryFragment =
            supportFragmentManager.findFragmentById(R.id.categoryCreationServiceLayout) as CategoryFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri = result.uri
                creationServicePresenter.createPhoto(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun showPremiumBlock(service: Service) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.premiumCreationServiceLayout, PremiumFragment.newInstance(service))
            .commit()
        premiumCreationServiceLayout.visibility = View.VISIBLE
    }

    override fun deletePhoto(photo: Photo) {
        MaterialAlertDialogBuilder(this, R.style.myDialogTheme)
            .setTitle("Внимание!")
            .setMessage(
                "Удалить фотографию?"
            )
            .setPositiveButton("Да") { _, _ ->
                creationServicePresenter.removePhoto(photo)
            }.setNegativeButton("Нет") { _, _ ->
            }.show()
    }

    override fun openPhoto() {
        val intent = Intent(this, PhotoDialogActivity::class.java)
        intent.putParcelableArrayListExtra(
            Photo.PHOTO,
            ArrayList(creationServicePresenter.getPhotosLink())
        )
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun updatePhotoFeed() {
        changeablePhotoAdapter.notifyDataSetChanged()
    }

    override fun hideMainBlock() {
        mainCreationServiceLinearLayout.visibility = View.GONE
        addServiceCreationServiceBtn.visibility = View.GONE
        continueCreationServiceBtn.visibility = View.VISIBLE
    }

    override fun showMainBlock() {
        mainCreationServiceLinearLayout.visibility = View.VISIBLE
        addServiceCreationServiceBtn.visibility = View.VISIBLE
        continueCreationServiceBtn.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showMoreTenImages() {
        Toast.makeText(this, "Должно быть меньше 10 фотографий", Toast.LENGTH_LONG).show()
    }

    override fun showNameInputError(error: String) {
        nameCreationServiceInput.error = error
        nameCreationServiceInput.requestFocus()
    }

    override fun showDescriptionInputError(error: String) {
        descriptionCreationServiceInput.error = error
        descriptionCreationServiceInput.requestFocus()
    }

    override fun showCostInputError(error: String) {
        costCreationServiceInput.error = error
        costCreationServiceInput.requestFocus()
    }

    override fun showCategoryInputError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun showAddressInputError(error: String) {
        addressCreationServiceInput.error = error
        addressCreationServiceInput.requestFocus()
    }

    private fun goToSchedule() {
        val intent = Intent(this, ScheduleActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    companion object {
        const val CODES = "codes"
        const val CODE = "code"
        const val COUNT = "count"
    }

}
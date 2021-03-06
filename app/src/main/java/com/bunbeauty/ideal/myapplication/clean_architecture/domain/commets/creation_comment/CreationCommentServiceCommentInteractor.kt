package com.bunbeauty.ideal.myapplication.clean_architecture.domain.commets.creation_comment

import com.bunbeauty.ideal.myapplication.clean_architecture.domain.commets.creation_comment.i_creation_comment.ICreationCommentServiceCommentInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.comments.CreationCommentPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.service_comment.InsertServiceCommentCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.comment.ServiceComment
import com.bunbeauty.ideal.myapplication.clean_architecture.data.repositories.ServiceCommentRepository

class CreationCommentServiceCommentInteractor
    (private val serviceCommentRepository: ServiceCommentRepository) :
    ICreationCommentServiceCommentInteractor, InsertServiceCommentCallback {

    private var rating = 0f
    private var review = ""

    private lateinit var creationCommentPresenterCallback: CreationCommentPresenterCallback

    override fun setRating(rating: Float) {
        this.rating = rating
    }

    override fun setReview(review: String) {
        this.review = review
    }

    override fun createServiceComment(
        serviceComment: ServiceComment,
        creationCommentPresenterCallback: CreationCommentPresenterCallback
    ) {
        this.creationCommentPresenterCallback = creationCommentPresenterCallback
        serviceComment.review = review
        serviceComment.rating = rating
        serviceCommentRepository.insert(serviceComment, this)
    }

    override fun returnCreatedCallback(obj: ServiceComment) {
        creationCommentPresenterCallback.updateServiceRating(obj)
    }

}
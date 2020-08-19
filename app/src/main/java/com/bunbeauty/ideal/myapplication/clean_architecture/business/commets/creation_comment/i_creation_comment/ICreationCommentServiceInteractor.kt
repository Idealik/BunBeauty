package com.bunbeauty.ideal.myapplication.clean_architecture.business.commets.creation_comment.i_creation_comment

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.comments.CreationCommentPresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.comment.ServiceComment

interface ICreationCommentServiceInteractor {
    fun updateService(
        serviceComment: ServiceComment,
        creationCommentPresenterCallback: CreationCommentPresenterCallback
    )
}
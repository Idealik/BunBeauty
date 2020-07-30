package com.bunbeauty.ideal.myapplication.cleanArchitecture.di

import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.ScheduleActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.chat.DialogsActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.chat.MessagesActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments.CreationCommentActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments.CurrentCommentActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments.ServiceCommentsActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.comments.UserCommentsActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.createService.CreationServiceActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.editing.EditProfileActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.editing.EditServiceActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.fragments.PremiumFragment
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.logIn.AuthorizationActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.logIn.RegistrationActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.logIn.VerifyPhoneNumberActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.profile.ProfileActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.searchService.MainScreenActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.service.ServiceActivity
import com.bunbeauty.ideal.myapplication.cleanArchitecture.mvp.activities.subscriptions.SubscriptionsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [],
    modules = [
        AppModule::class,
        FirebaseModule::class,
        InteractorModule::class,
        RepositoryModule::class]
)
interface AppComponent {
    fun inject(authorizationActivity: AuthorizationActivity)
    fun inject(verifyPhoneNumberActivity: VerifyPhoneNumberActivity)
    fun inject(registrationActivity: RegistrationActivity)
    fun inject(profileActivity: ProfileActivity)
    fun inject(creationServiceActivity: CreationServiceActivity)
    fun inject(premiumFragment: PremiumFragment)
    fun inject(mainScreenActivity: MainScreenActivity)
    fun inject(serviceActivity: ServiceActivity)
    fun inject(editProfileActivity: EditProfileActivity)
    fun inject(scheduleActivity: ScheduleActivity)
    fun inject(dialogsActivity: DialogsActivity)
    fun inject(messagesActivity: MessagesActivity)
    fun inject(subscriptionsActivity: SubscriptionsActivity)
    fun inject(editServiceActivity: EditServiceActivity)
    fun inject(userCommentsActivity: UserCommentsActivity)
    fun inject(currentCommentActivity: CurrentCommentActivity)
    fun inject(creationUserCommentActivity: CreationCommentActivity)
    fun inject(serviceCommentsActivity: ServiceCommentsActivity)
}

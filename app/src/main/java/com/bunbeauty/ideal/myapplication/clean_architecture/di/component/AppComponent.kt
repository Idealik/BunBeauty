package com.bunbeauty.ideal.myapplication.clean_architecture.di.component

import android.app.Application
import android.content.Intent
import com.bunbeauty.ideal.myapplication.clean_architecture.App
import com.bunbeauty.ideal.myapplication.clean_architecture.di.module.*
import com.bunbeauty.ideal.myapplication.clean_architecture.di.scope.AppScope
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.ScheduleActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.SessionsActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.chat.DialogsActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.chat.MessagesActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.comments.CreationCommentActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.comments.CurrentCommentActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.comments.ServiceCommentsActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.comments.UserCommentsActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.create_service.CreationServiceActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.editing.EditProfileActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.editing.EditServiceActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.log_in.AuthorizationActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.log_in.RegistrationActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.log_in.VerifyPhoneNumberActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.profile.ProfileActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.search_service.MainScreenActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.service.ServiceActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.subscriptions.SubscriptionsActivity
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.fragments.PremiumFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
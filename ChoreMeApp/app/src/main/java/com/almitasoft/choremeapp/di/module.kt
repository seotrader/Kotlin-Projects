package com.almitasoft.choremeapp.di
import android.content.Context
import com.almitasoft.choremeapp.Notifications.NotificationSender
import com.almitasoft.choremeapp.data.FireBaseManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.dsl.module

val appModule = module {
    single <NotificationSender> { (context: Context) -> NotificationSender(context)}
    factory <FireBaseManager>{FireBaseManager()}

}

class NotificationServiceComponent : KoinComponent {
    val service by inject<NotificationSender>()
}


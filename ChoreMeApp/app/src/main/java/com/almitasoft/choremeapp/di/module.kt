package com.almitasoft.choremeapp.di
import android.content.Context
import com.almitasoft.choremeapp.Notifications.NotificationSender
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.data.FireBaseManager
import com.almitasoft.choremeapp.ui.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.KoinComponent
import org.koin.core.context.GlobalContext.get
import org.koin.core.inject
import org.koin.dsl.module

val appModule = module {

    single <NotificationSender> { (context: Context) -> NotificationSender(context)}
    factory {FireBaseManager()}
    factory {FireBaseManager() as FireBaseInterface}
   // factory {FireBaseManager() as FireBaseManager}
    viewModel{NotificationsViewModel(get())}

}

class NotificationServiceComponent : KoinComponent {
    val service by inject<NotificationSender>()
}


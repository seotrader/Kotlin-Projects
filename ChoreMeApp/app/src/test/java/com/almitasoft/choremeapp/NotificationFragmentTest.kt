package com.almitasoft.choremeapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.almitasoft.choremeapp.data.FireBaseInterface
import com.almitasoft.choremeapp.di.appModule
import com.almitasoft.choremeapp.model.AddFriendNotification
import com.almitasoft.choremeapp.ui.notifications.NotificationsViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.test.KoinTest
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.BDDMockito.given
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class NotificationFragmentTest : KoinTest{
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // You can start your Koin configuration
        startKoin { modules(appModule) }
        MockitoAnnotations.initMocks(this)
    }

    private var testObserable : Observable<ArrayList<AddFriendNotification>>?=null

    @Test
    fun getNotificationsListSuccess(){
        val lv : NotificationsViewModel by inject()

        declareMock<FireBaseInterface>{
            given(this.getNotifications2()).willReturn(testObserable)
        }

        val notificationList = arrayListOf<AddFriendNotification>()

        notificationList.add(AddFriendNotification("XXX ADDED YYY"))
        notificationList.add(AddFriendNotification("BBB ADDED TASK BBB"))
        notificationList.add(AddFriendNotification("CCC ADDED DDD"))

        testObserable = Observable.just(notificationList)

        lv.refreshData()

        Assert.assertEquals(3, lv.notificationList.value?.size)
    }

    @Before
    fun setUpRxSchedulers(){
        val immediate = object:Scheduler(){
            override fun createWorker() : Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }

            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }
        }
        RxJavaPlugins.setInitSingleSchedulerHandler{ scheduler->immediate}
        RxJavaPlugins.setInitComputationSchedulerHandler{scheduler->immediate}
        RxJavaPlugins.setInitNewThreadSchedulerHandler {  scheduler->immediate}
        RxJavaPlugins.setInitSingleSchedulerHandler {  scheduler->immediate}
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler->immediate}

    }

    @After
    fun after(){
        stopKoin()
    }

}
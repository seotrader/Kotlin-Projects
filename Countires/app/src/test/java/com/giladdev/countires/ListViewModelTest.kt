package com.giladdev.countires

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.giladdev.countires.Model.CountriesService
import com.giladdev.countires.Model.Country
import com.giladdev.countires.ViewModel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {
    @get:Rule

    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countiresService : CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    // test success path
    fun getCountiresSuccess() {
        val country = Country("country name", "capital","url")
        val countiresList = arrayListOf(country)
        testSingle = Single.just(countiresList)

        `when`(countiresService.getCountires()).thenReturn(testSingle)
        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.countires.value?.size)
        Assert.assertEquals(false, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test

    fun GetCountiresFail(){
        testSingle = Single.error(Throwable())

        `when`(countiresService.getCountires()).thenReturn(testSingle)
        listViewModel.refresh()


        Assert.assertEquals(true, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)


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
        RxJavaPlugins.setInitSingleSchedulerHandler{scheduler->immediate}
        RxJavaPlugins.setInitComputationSchedulerHandler{scheduler->immediate}
        RxJavaPlugins.setInitNewThreadSchedulerHandler {  scheduler->immediate}
        RxJavaPlugins.setInitSingleSchedulerHandler {  scheduler->immediate}
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{scheduler->immediate}

        }



    }
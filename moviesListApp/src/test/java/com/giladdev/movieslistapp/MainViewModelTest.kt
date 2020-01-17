package com.giladdev.movieslistapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.giladdev.movieslistapp.model.Movie
import com.giladdev.movieslistapp.model.MoviesObject
import com.giladdev.movieslistapp.model.MoviesService
import com.giladdev.movieslistapp.viewmodel.MainViewModel
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

class MainViewModelTest {
    class ListViewModelTest {
        @get:Rule

        var rule = InstantTaskExecutorRule()

        @Before
        fun setUp() {
            MockitoAnnotations.initMocks(this)
        }

        @Mock
        var moviesService = MoviesService()

        @InjectMocks
        var mainViewModel = MainViewModel()

        private var testSingle: Single<MoviesObject>? = null

        @Test
        // test success path
        fun getMoviesSuccess() {
            val moviesObject = MoviesObject()

            val firstMovie =
                Movie("11241","Jumanji: welcome to the jungle","2017",
                    "Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg")

            val secondMovie =
                Movie("11241","Jumanji: welcome to the jungle","2017",
                    "Action","https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg")

               moviesObject.moviesList = mutableListOf<Movie>(firstMovie,
                   secondMovie
            )

            testSingle = Single.just(moviesObject)

            `when`(moviesService.getMovies()).thenReturn(testSingle)
            mainViewModel.refresh()


            Assert.assertEquals(firstMovie, mainViewModel.moviesList.value!![0])
            Assert.assertEquals(secondMovie, mainViewModel.moviesList.value!![1])
            Assert.assertEquals(2, mainViewModel.moviesList.value?.size)
            Assert.assertEquals("OK", mainViewModel.moviesError.value)
            Assert.assertEquals(false, mainViewModel.moviesLoading.value)
        }

        @Test

        fun getMoviesError(){
            testSingle = Single.error(Throwable("ERROR"))

            `when`(moviesService.getMovies()).thenReturn(testSingle)
            mainViewModel.refresh()


            Assert.assertEquals("ERROR", mainViewModel.moviesError.value)
            Assert.assertEquals(false, mainViewModel.moviesLoading.value)
        }

        @Before
        fun setUpRxSchedulers(){
            val immediate = object: Scheduler(){
                override fun createWorker() : Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }

                override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                    return super.scheduleDirect(run, 0, unit)
                }
            }
            RxJavaPlugins.setInitSingleSchedulerHandler{ scheduler->immediate}
            RxJavaPlugins.setInitComputationSchedulerHandler{ scheduler->immediate}
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler->immediate}
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler->immediate}
            RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler->immediate}

        }
    }
}
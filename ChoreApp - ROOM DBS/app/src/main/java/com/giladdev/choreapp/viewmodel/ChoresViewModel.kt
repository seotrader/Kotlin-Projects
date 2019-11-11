package com.giladdev.choreapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giladdev.choreapp.model.AppDataBase
import com.giladdev.choreapp.model.ChoresEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ChoresViewModel: ViewModel() {
    var choresList = MutableLiveData<List<ChoresEntity>>()
    var deleteChore = MutableLiveData<Boolean>()

    var choresDB: AppDataBase?= null
    var mContext : Context?=null

    private val disposable = CompositeDisposable()


    fun refresh()
    {
        deleteChore.value = false
        choresDB = mContext?.let { AppDataBase.getAppDataBase(it) }
        FetchChores()
    }

    fun UpdateChore(chore: ChoresEntity){
        Observable.fromCallable( {choresDB?.ChoresDao()?.updateChore(chore)})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {

                }

                override fun onComplete() {
                    Log.d("RXJAVA","Update Chore Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("RXJAVA","Error = ${e.message}")
                }

            })

    }

    fun AddChore(chore : ChoresEntity)
    {
        Observable.fromCallable( {choresDB?.ChoresDao()?.insertChore(chore)})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {

                }

                override fun onComplete() {
                    Log.d("RXJAVA","Add Chore Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("RXJAVA","Error = ${e.message}")
                }

            })

    }

    fun DeleteChore(chore : ChoresEntity)
    {
        Observable.fromCallable( {choresDB?.ChoresDao()?.deleteChore(chore)})
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Unit>(){
                override fun onNext(t: Unit) {
                    deleteChore.value = true
                }

                override fun onComplete() {
                    Log.d("RXJAVA","Completed")
                }

                override fun onError(e: Throwable) {
                    Log.d("RXJAVA","Error = ${e.message}")
                }

            })

    }

    fun FetchChores()
    {

        Observable.fromCallable( {choresDB?.ChoresDao()?.getChores()})
        .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<List<ChoresEntity>>(){
                override fun onComplete() {
                    Log.d("RXJAVA","Completed")
                }

                override fun onNext(t: List<ChoresEntity>) {
                    choresList.value = t
                }

                override fun onError(e: Throwable) {
                    Log.d("RXJAVA","Error = ${e.message}")
                }

            })

    }
}


package com.giladdev.rickyandmarty.Util

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageFetcher(private val picasso: Picasso) {

    fun loadImage(baseUrl: String) : Single<Bitmap>{
        return Single
                .create(ImageFetcherSingleSubscribe(picasso, baseUrl))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
////                //.toObservable()
                //.onErrorResumeNext(Observable.empty<Bitmap>())
    }

}


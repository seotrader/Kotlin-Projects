package com.giladdev.rickyandmarty.Util

import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageFetcherSingleSubscribe(private val picasso: Picasso,
                                  private val url: String) : SingleOnSubscribe<Bitmap> {

    private val runningTargets = mutableListOf<Target>()

    override fun subscribe(emitter: SingleEmitter<Bitmap>) {
        val target = CustomImageLoadTarget(emitter) {
            removeTargetAndCancelRequest(it)
        }

        runningTargets.add(target)
        picasso.load(url)
                .into(target)

    }

    private fun removeTargetAndCancelRequest(target: Target) {
        picasso.cancelRequest(target)
        runningTargets.remove(target)
    }
}
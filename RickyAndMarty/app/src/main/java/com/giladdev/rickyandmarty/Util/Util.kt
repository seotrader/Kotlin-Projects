package com.giladdev.rickyandmarty.Util

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giladdev.rickyandmarty.R
import java.lang.Exception

fun getProgressDrawable(context : Context) =
     CircularProgressDrawable(context).apply {
        centerRadius = 50f
        strokeWidth = 10f
        start()
    }


fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    try {
        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .into(this)
    }
    catch (e : Exception)
    {
        Log.d("Excepion",e.message.toString())
    }

}
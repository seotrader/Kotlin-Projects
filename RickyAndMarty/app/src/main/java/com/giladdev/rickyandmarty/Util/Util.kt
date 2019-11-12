package com.giladdev.rickyandmarty.Util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.giladdev.rickyandmarty.R
import java.io.ByteArrayOutputStream
import java.lang.Exception
import android.util.Base64
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.giladdev.rickyandmarty.model.CharecterDBEntity
import com.giladdev.rickyandmarty.viewmodel.ListViewModel




//fun isOnline(context: Context): Boolean {
//    val cm : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//
//
//
//    if (activeNetwork?.isConnectedOrConnecting == true){
//        return true
//
//    }
//    return false
//}

fun getProgressDrawable(context : Context) =
    CircularProgressDrawable(context).apply {
        centerRadius = 50f
        strokeWidth = 10f
        start()
    }

fun ImageView.imageToString() : String{

    val bitMap = this.drawable.toBitmap()
    var stream = ByteArrayOutputStream()
    bitMap.compress(Bitmap.CompressFormat.PNG,100,stream)
    var byte_arr : ByteArray = stream.toByteArray()
    val imageStr = Base64.encodeToString(byte_arr, 1)
    return imageStr
}

fun StringtoBitMap(s : String) : Bitmap{

    val decodedString = Base64.decode(s, Base64.DEFAULT)
    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return decodedByte
}

fun bitMapToString(b : Bitmap) : String{
    var stream = ByteArrayOutputStream()
    b.compress(Bitmap.CompressFormat.PNG,100,stream)
    var byte_arr : ByteArray = stream.toByteArray()
    val imageStr = Base64.encodeToString(byte_arr, 1)
    return imageStr
}

// Async writing the images to DBS. images saved as Strings
fun LoadImageToDBS(context : Context,characterDB : CharecterDBEntity, listView : ListViewModel) {

    var characterToUpdate = characterDB

    try {
        Glide.with(context)
            .asBitmap()
            .load(characterDB.url)
            .listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.run{
                        characterToUpdate.imageRawData = bitMapToString(this)
                        listView.updateCharacter(characterToUpdate)
                    }

                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }
            )
            .submit()
    }
    catch (e : Exception)
    {
        Log.d("Excepion",e.message.toString())
    }

}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    try {
        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(uri)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {

                    return false
                }})

            .into(this)

    }
    catch (e : Exception)
    {
        Log.d("Excepion",e.message.toString())
    }

}
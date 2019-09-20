package com.gilad.imagefilterapp

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colorsArray = arrayOf(Color.BLACK, Color.BLUE,Color.GREEN,Color.DKGRAY,Color.RED,Color.WHITE,Color.YELLOW,Color.TRANSPARENT,Color.GRAY)

        var porterModes = arrayListOf(PorterDuff.Mode.OVERLAY, PorterDuff.Mode.ADD, PorterDuff.Mode.DARKEN,PorterDuff.Mode.LIGHTEN, PorterDuff.Mode.MULTIPLY)


        dogImageView.setOnClickListener{
            dogImageView.setColorFilter (colorsArray[GetRandom(colorsArray.size)],porterModes[GetRandom(porterModes.size)])

        }
}

    fun GetRandom(arraySize: Int): Int
    {
        var rand = Random()
        var randomNum = rand.nextInt(arraySize)

        return randomNum
    }
}

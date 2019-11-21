package com.giladdev.rickyandmarty.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.Util.StringtoBitMap
import kotlinx.android.synthetic.main.activity_show_image.*
import java.lang.Exception

class ShowImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        var data = intent.extras

        data?.run{
            var name = data.get("name")
            var sex = data.get("sex")
            var imageRaw = data.get("imageRaw")

            picNameEditText.text = name.toString()
            picSexEditText.text = sex.toString()

            bigImageView.setImageResource(R.drawable.ic_launcher_background)
            try{
                bigImageView.setImageBitmap( StringtoBitMap(imageRaw.toString()) )
            }
            catch (e: Exception){
                Log.d("image.setImageBitmap","Exception = ${e.message}")
            }

        }

        backButton.setOnClickListener {

            finish()
        }
    }


}

package com.giladdev.rickyandmarty.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.rickyandmarty.model.Character
import com.giladdev.rickyandmarty.R
import org.w3c.dom.Text
import java.net.URL

class CharacterListAdaptor(private val list:ArrayList<Character>,
                           private val context:Context) : RecyclerView.Adapter<CharacterListAdaptor.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(character : Character)
        {
            var name : TextView = itemView.findViewById(R.id.nameTextView) as TextView
            var gender : TextView = itemView.findViewById(R.id.GenderTextView) as TextView
            var image : ImageView = itemView.findViewById(R.id.imageView) as ImageView

            name.text = character.name
            gender.text = character.gender

            try {

                // download the image
                var imageDownloader: DownLoadImageTask =
                    DownLoadImageTask(image, character.image.toString())

                imageDownloader.execute()
            }
            catch (e: Exception) { // Catch the download exception
                Log.d("Exception DownLoadImageTask ",e.message)

            }

            itemView.setOnClickListener {
                Toast.makeText(context, name.text,Toast.LENGTH_LONG ).show()
            }
        }

    }

    // Class to download an image from url and display it into an image view
     private class DownLoadImageTask(internal val imageView: ImageView, val imageUrl : String) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {

            var img : Bitmap?=null

            try {
                val inputStream = URL(imageUrl).openStream()
                    img = BitmapFactory.decodeStream(inputStream)

            } catch (e: Exception) { // Catch the download exception
                e.printStackTrace()
                return null
            }

            return img
        }
        override fun onPostExecute(result: Bitmap?) {
            if(result!=null){
                // Display the downloaded image into image view
                Log.d("Image Download","download success $imageUrl" )
                imageView.setImageBitmap(result)
            }else{
                Toast.makeText(imageView.context,"Error downloading",Toast.LENGTH_SHORT).show()
                Log.d("Image Download","Error Downloading $imageUrl" )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindItem(list[position])
    }

}
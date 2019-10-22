package com.giladdev.rickyandmarty.UI

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
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.giladdev.rickyandmarty.model.Character
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.Util.getProgressDrawable
import com.giladdev.rickyandmarty.Util.loadImage
import com.giladdev.rickyandmarty.model.CharacterList
import kotlinx.android.synthetic.main.list_row.view.*
import java.net.URL

class CharacterListAdaptor(private val list:ArrayList<Character>,private val context:Context) : RecyclerView.Adapter<CharacterListAdaptor.ViewHolder>() {
    val lastLine = MutableLiveData<Boolean>()
    var lastNumberOfLinesLoaded = 0

    init{
        list.clear()
        lastLine.value = false
    }
    fun UpdateCharacters(newCharacters: CharacterList)
    {
        list.addAll(newCharacters.characterList!!.toTypedArray())
        notifyDataSetChanged()
    }



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
            var image : ImageView = itemView.imageView


            val progressDrawable : CircularProgressDrawable = getProgressDrawable(itemView.context)


            name.text = character.name
            gender.text = character.gender
            image.loadImage(character.image,progressDrawable )
            image.visibility = View.VISIBLE
/*
            try {

                // download the image
                var imageDownloader: DownLoadImageTask =
                    DownLoadImageTask(
                        image,
                        character.image.toString()
                    )

                imageDownloader.execute()
            }
            catch (e: Exception) { // Catch the download exception
                Log.d("Exception DownLoadImageTask ",e.message)

            }
*/
            itemView.setOnClickListener {
                Toast.makeText(it.context, name.text,Toast.LENGTH_LONG ).show()
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if ((position == list.size-1) && (lastNumberOfLinesLoaded != position))
        {
            lastNumberOfLinesLoaded = list.size-1
            lastLine.value = true
        }
        return holder.bindItem(list[position])
    }

}


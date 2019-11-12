package com.giladdev.rickyandmarty.UI

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.rickyandmarty.model.Character
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.Util.getProgressDrawable
import com.giladdev.rickyandmarty.Util.loadImage
import com.giladdev.rickyandmarty.model.CharacterList
import com.giladdev.rickyandmarty.viewmodel.ConnectionMode
import kotlinx.android.synthetic.main.list_row.view.*
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.giladdev.rickyandmarty.Util.StringtoBitMap
import java.lang.Exception


class CharacterListAdaptor(private val list:ArrayList<Character>,private val context:Context) : RecyclerView.Adapter<CharacterListAdaptor.ViewHolder>() {
    val lastLine = MutableLiveData<Boolean>()
    var lastNumberOfLinesLoaded = 0
    var connMode : ConnectionMode = ConnectionMode.OFFLINE
    var areAllImagesLoaded = false

    init{
        list.clear()
        lastLine.value = false
    }
    fun updateCharacters(newCharacters: CharacterList)
    {
        list.clear()
        list.addAll(newCharacters.characterList.toTypedArray())
        lastLine.value = false
        notifyDataSetChanged()

        if (connMode == ConnectionMode.OFFLINE) {
            lastNumberOfLinesLoaded=0
        }
    }

    fun clear(){
        var size = list.size
        lastNumberOfLinesLoaded = 0
        list.clear()
        notifyItemRangeRemoved(0,size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount() = list.size


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {


        fun bindItem(character : Character)
        {
            var name : TextView = itemView.findViewById<TextView>(R.id.nameTextView)
            var gender : TextView = itemView.findViewById<TextView>(R.id.GenderTextView)
            var image : ImageView = itemView.imageView
            var constraint = itemView.findViewById<ConstraintLayout>(R.id.constraint_id)


            val progressDrawable = getProgressDrawable(itemView.context)

            name.text = character.name
            gender.text = character.gender
            if (connMode==ConnectionMode.ONLINE){
                image.loadImage(character.image,progressDrawable)

            }
            else{
                image.setImageResource(R.drawable.ic_launcher_background)
                try{
                    image.setImageBitmap(character.imageRawData?.let { StringtoBitMap(it) })
                }
                catch (e: Exception){
                    Log.d("image.setImageBitmap","Exception = ${e.message}")
                }
            }

            image.visibility = View.VISIBLE

            if (connMode == ConnectionMode.OFFLINE)
                constraint.setBackgroundColor(Color.RED)
            else
                constraint.setBackgroundColor(Color.BLUE)

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


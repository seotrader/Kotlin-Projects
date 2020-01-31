package com.almitasoft.choremeapp.ui.addTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PickUserAdapter(var userList : ArrayList<User>,var userPicked : UserPicked) :
    RecyclerView.Adapter<PickUserAdapter.PickUserViewHolder>() {

    fun updateUsersList(list : ArrayList<User>){
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

    inner class PickUserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItem(user : User){
            var displayName = itemView.findViewById<TextView>(R.id.tvFriendDisplayName)
            var image = itemView.findViewById<CircleImageView>(R.id.IVFriend)
            var btnAdd = itemView.findViewById<Button>(R.id.btnAddToTask)
            displayName.text = user.displayName

            image.setImageResource(R.drawable.default_avata)

            user.thumb_image_url?.run{
                Picasso.get().load(user.thumb_image_url)
                    .placeholder(R.drawable.default_avata)
                    .into(image)
            }

            btnAdd.setOnClickListener {
                userPicked.onUserPicker(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickUserViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_friend, parent, false)

        return PickUserViewHolder(view)

    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: PickUserViewHolder, position: Int) {
        holder.bindItem(userList[position])
    }
}
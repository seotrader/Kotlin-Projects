package com.almitasoft.choremeapp.ui.addusers

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.User
import com.squareup.picasso.Picasso

class SearchUsersAdapter(var usersList:ArrayList<User>,
                         var searchUserFrgmt : SearchUsers) : RecyclerView.Adapter<SearchUsersAdapter.ViewHolder>(), Filterable {

    var savedFullUserList = arrayListOf<User>()

    var usersFilter = object:Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredUsersList = arrayListOf<User>()

            if (constraint == null || constraint.length==0)
                filteredUsersList.addAll(savedFullUserList)
            else{
                var filterPattern = constraint.toString().toLowerCase().trim()

                var filteredList = usersList.filter {
                    (it.displayName.toLowerCase().contains(filterPattern) == true) ||
                            (it.userEmail!!.toLowerCase().contains(filterPattern) == true)

                }

                filteredUsersList.addAll(filteredList)
            }

                var results = FilterResults()
    
                results.values = filteredUsersList
                return results
        }


        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            usersList.clear()
            var result: ArrayList<User> = results!!.values as ArrayList<User>

            usersList.addAll(result)

            if ( constraint==null ||  constraint.length==0)
            {
                if (usersList.isEmpty()){
                    usersList.addAll(savedFullUserList)
                }

            }
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return usersFilter
    }

    inner class ViewHolder (itemView : View) :  RecyclerView.ViewHolder(itemView){

        fun bindItem(user : User){
            var displayName = itemView.findViewById<TextView>(R.id.tvDisplayName)
            var image = itemView.findViewById<ImageView>(R.id.IVUser)
            var button = itemView.findViewById<Button>(R.id.btnFriendRequest)

            displayName.text = user.displayName

            Picasso.get().load(user.thumb_image_url)
                .placeholder(R.drawable.default_avata)
                .into(image)

            button.setOnClickListener {
                searchUserFrgmt.addUser(user)
                Toast.makeText(searchUserFrgmt.context,"Sent friend request to ${user.displayName}",
                    Toast.LENGTH_SHORT).show()
            }

            // TODO : set the image as well
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usertoadd, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = usersList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(usersList[position])
    }


}
package com.giladdev.choreapp.ui

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.choreapp.R
import com.giladdev.choreapp.model.Chore
import com.giladdev.choreapp.model.ChoresDataBaseHandler
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListAdapter(private val list:ArrayList<Chore>,
                       private val context: Context) : RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreListAdapter.ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(context)
                 .inflate(R.layout.list_row,parent,false)

        return ViewHolder(view,context,list)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChoreListAdapter.ViewHolder, position: Int) {
        holder.bindViews(list[position])

    }

    inner class ViewHolder(itemView: View,context:Context, list : ArrayList<Chore>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mList = list
        var choreName = itemView.findViewById(R.id.listChoreNameTextView) as TextView
        var assignedBy = itemView.findViewById(R.id.listAssignedByTextView) as TextView
        var assignedTo = itemView.findViewById(R.id.listAssignedToTextView) as TextView
        var assignedDate = itemView.findViewById(R.id.listDateTextView) as TextView

        var mContext = context

        var deleteButton = itemView.findViewById(R.id.listDeleteButton) as Button
        var editButton = itemView.findViewById(R.id.listEditButton) as Button


        fun bindViews(chore: Chore) {
            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedTo.text = chore.assignedTo
            assignedDate.text = chore.GetTimeAsString()

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)


        }

        override fun onClick(v: View?) {

            var mPosition: Int = adapterPosition
            var clone = mList[mPosition]

            when (v!!.id) {
                deleteButton.id -> {
                    deleteChore(clone.id!!)
                    mList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                editButton.id -> {
                    editChore(clone)
                }
            }
        }

        fun deleteChore(id: Int) {
            var db: ChoresDataBaseHandler = ChoresDataBaseHandler(mContext)

            db.deleteChore(id)
        }

        fun editChore(chore: Chore) {

            lateinit var dialogBuilder: AlertDialog.Builder
            lateinit var dialog: AlertDialog
            var dbHandler = ChoresDataBaseHandler(context)
            var mPosition: Int = adapterPosition

            var view = LayoutInflater.from(context).inflate(R.layout.popup,null)

            var choreName = view.popEnterChoreText
            var assignedBy = view.popEnterAssignedBy
            var assignedTo = view.popEnterAssignedTo
            var saveButton = view.popUpSaveButton

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder.create()
            dialog.show()

            saveButton.setOnClickListener {
                var name = choreName.text.toString().trim()
                var aBy = assignedBy.text.toString().trim()
                var aTo = assignedTo.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                    && (!TextUtils.isEmpty(aBy))
                    && (!TextUtils.isEmpty(aTo))
                ) {
                   // var chore = Chore()
                    chore.choreName = name
                    chore.assignedBy = aBy
                    chore.assignedTo = aTo
                    //chore.id = mPosition

                    dbHandler.UpdateChore(chore)
                    notifyItemChanged(adapterPosition, chore)

                    dialog.dismiss()
                } else {


                }

            }

        }

    }
}
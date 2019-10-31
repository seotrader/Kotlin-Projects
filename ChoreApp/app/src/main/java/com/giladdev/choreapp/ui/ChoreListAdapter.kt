package com.giladdev.choreapp.ui

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.giladdev.choreapp.R
import com.giladdev.choreapp.Util.GetTimeAsString
import com.giladdev.choreapp.model.ChoresEntity
import kotlinx.android.synthetic.main.popup.view.*

class ChoreListAdapter(private val list:ArrayList<ChoresEntity>,
                       private val context: ChoreListActivity) : RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    var viewHolder : ViewHolder ?=null

    fun UpdateChores(newCharacters: List<ChoresEntity>)
    {
        list.clear()
        list.addAll(newCharacters.toTypedArray())
        list.reverse()
        notifyDataSetChanged()
    }

    fun ReverseList()
    {
        list.reverse()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreListAdapter.ViewHolder {
        // create our view from the XML file
        val view = LayoutInflater.from(context)
                 .inflate(R.layout.list_row,parent,false)

        viewHolder = ViewHolder(view,context,list)

        return viewHolder as ViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChoreListAdapter.ViewHolder, position: Int) {
        holder.bindViews(list[position])

    }

    inner class ViewHolder(itemView: View,context:Context, list : ArrayList<ChoresEntity>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var mList = list
        var choreName = itemView.findViewById(R.id.listChoreNameTextView) as TextView
        var assignedBy = itemView.findViewById(R.id.listAssignedByTextView) as TextView
        var assignedTo = itemView.findViewById(R.id.listAssignedToTextView) as TextView
        var assignedDate = itemView.findViewById(R.id.listDateTextView) as TextView

        var mContext = context

        var deleteButton = itemView.findViewById(R.id.listDeleteButton) as Button
        var editButton = itemView.findViewById(R.id.listEditButton) as Button


        fun bindViews(chore: ChoresEntity) {
            choreName.text = chore.choreName
            assignedBy.text = chore.assignedBy
            assignedTo.text = chore.assignedTo
            assignedDate.text = GetTimeAsString(chore.timerAssigned)

            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)


        }

        fun ConfirmDelete(chore : ChoresEntity, adapterPosition : Int)
        {
            // Initialize a new instance of
            val builder = AlertDialog.Builder(context)
            // Set the alert dialog title
            builder.setTitle("DELETE A CHORE")

            // Display a message on alert dialog
            builder.setMessage("Are You Sure You Want To Delete This Chore?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES"){ dialog, _ ->

                context.choresViewModel.DeleteChore(chore)
                mList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                dialog.dismiss()
            }

            // Display a negative button on alert dialog
            builder.setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            dialog.setIcon(R.drawable.warning)
            // Display the alert dialog on app interface
            dialog.show()

        }

        override fun onClick(v: View?) {

            var mPosition: Int = adapterPosition


            when (v!!.id) {
                deleteButton.id -> {
                        if (mPosition != -1) {
                            var clone = mList[mPosition]
                            ConfirmDelete(clone,adapterPosition)
                        }
                }
                editButton.id -> {
                    var clone = mList[mPosition]
                    editChore(clone)
                }
            }
        }

        fun editChore(chore: ChoresEntity) {

            lateinit var dialogBuilder: AlertDialog.Builder
            lateinit var dialog: AlertDialog

            var view = LayoutInflater.from(context).inflate(R.layout.popup,null)
            var updatedChore = ChoresEntity(chore.id, chore.choreName,chore.assignedBy,chore.assignedTo,chore.timerAssigned)

            var choreName = view.popEnterChoreText
            var assignedBy = view.popEnterAssignedBy
            var assignedTo = view.popEnterAssignedTo
            var saveButton = view.popUpSaveButton

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder.create()

            choreName.setText(updatedChore.choreName)
            assignedBy.setText(updatedChore.assignedBy)
            assignedTo.setText(updatedChore.assignedTo)

            dialog.show()

            saveButton.setOnClickListener {
                var name = choreName.text.toString().trim()
                var aBy = assignedBy.text.toString().trim()
                var aTo = assignedTo.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                    && (!TextUtils.isEmpty(aBy))
                    && (!TextUtils.isEmpty(aTo))
                ) {
                    updatedChore.choreName = name
                    updatedChore.assignedBy = aBy
                    updatedChore.assignedTo = aTo

                    context.choresViewModel.UpdateChore(updatedChore)
                    mList.set(adapterPosition,updatedChore)
                    notifyItemChanged(adapterPosition, updatedChore)
                    dialog.dismiss()
                } else {


                }

            }

        }

    }
}
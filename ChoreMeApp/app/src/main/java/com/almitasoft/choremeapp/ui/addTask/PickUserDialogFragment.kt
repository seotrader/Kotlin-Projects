package com.almitasoft.choremeapp.ui.addTask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.model.User
import com.almitasoft.choremeapp.ui.SharedViewModel
import kotlinx.android.synthetic.main.pick_user_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

interface UserPicked{
    fun onUserPicker(user : User)
}

class PickUserDialogFragment(var userPicked : UserPicked) : DialogFragment() {

    lateinit var pickUserAdapter : PickUserAdapter
    val sharedViewModel: SharedViewModel by sharedViewModel()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.pick_user_fragment,container)

        pickUserAdapter = PickUserAdapter(arrayListOf(),userPicked)

        var recyclerView = rootView.findViewById<RecyclerView>(R.id.pickUserRV)
        var close = rootView.findViewById<ImageView>(R.id.IVClose)

        recyclerView.apply {
            adapter = pickUserAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        close.setOnClickListener {
            dismiss()
        }

        setListeners()

        return rootView
    }

    private fun setListeners(){
        sharedViewModel.getFriendsList().observe(this, object: Observer<ArrayList<User>> {

            override fun onChanged(t: ArrayList<User>?) {
                t?.let{
                        pickUserAdapter.updateUsersList(it)
                }

            }
        })
    }

}
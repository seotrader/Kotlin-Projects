package com.giladdev.rickyandmarty.UI

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.giladdev.rickyandmarty.R

class ConnectivityDialogFragment : DialogFragment() {

    val optionsArray = arrayOf("Online","Offline")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.connectivityDialogTitle)
            builder.setSingleChoiceItems(optionsArray,1, DialogInterface.OnClickListener {dialog, which ->
                when (which)
                {
                    0 -> {
                        Toast.makeText(context,"Offline", Toast.LENGTH_LONG).show()

                    }
                    1-> {
                        Toast.makeText(context,"Online", Toast.LENGTH_LONG).show()
                    }

                }
            }

                )
            // Create the AlertDialog object and return it
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}
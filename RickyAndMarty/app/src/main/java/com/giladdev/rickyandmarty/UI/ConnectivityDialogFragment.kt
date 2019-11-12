package com.giladdev.rickyandmarty.UI
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.giladdev.rickyandmarty.R
import com.giladdev.rickyandmarty.viewmodel.ConnectionMode

class ConnectivityDialogFragment(var appActivity : MainActivity,
                                 currentMode:ConnectionMode) : DialogFragment() {

    val optionsArray = arrayOf("Offline","Online")
    var conneMode = MutableLiveData<ConnectionMode>()

    init{
        conneMode.value = currentMode
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.connectivityDialogTitle)
            builder.setNeutralButton("CLOSE",DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })

            var checkedItem = if ( conneMode.value==ConnectionMode.OFFLINE) 0 else 1

            builder.setSingleChoiceItems(optionsArray,checkedItem, DialogInterface.OnClickListener {_, which ->
                when (which)
                {
                    0 -> {
                        Toast.makeText(context,"Offline", Toast.LENGTH_LONG).show()
                        conneMode.value = ConnectionMode.OFFLINE

                    }
                    1-> {

                        if (appActivity.isConnectedToInternet) {
                            Toast.makeText(context, "Online", Toast.LENGTH_LONG).show()
                            conneMode.value = ConnectionMode.ONLINE
                        } else  {
                            Toast.makeText(context,"NO INTERNET !", Toast.LENGTH_LONG).show()
                            conneMode.value = ConnectionMode.OFFLINE
                        }

                        }

                    }
                }

                )
                // Create the AlertDialog object and return it
                builder.create()
            }?: throw IllegalStateException("Activity cannot be null")
        }
    }
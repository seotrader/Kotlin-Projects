package com.almitasoft.choremeapp.ui.Settings

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.almitasoft.choremeapp.R
import com.almitasoft.choremeapp.model.CurrentUser
import com.almitasoft.choremeapp.ui.MainActivity
import com.almitasoft.choremeapp.ui.SharedViewModel
import com.almitasoft.choremeapp.ui.notifications.NotificationsViewModel
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.user_settings_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception


class UserSettingsFragment : Fragment() {

    private val GALLERY_ID = 1
    private var mainActivity : Activity ?= null

    private val userSettingsViewModel : UserSettingsViewModel by viewModel()
    private lateinit var sharedViewModel: SharedViewModel

    companion object {
        fun newInstance() = UserSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainActivity = activity as MainActivity
        setUIListeners()

        setFireBaseListener()

        activity?.let{
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        if (sharedViewModel.isUserConnected()){
            displayNameTVID.text = CurrentUser.displanyName

        }
    }
    private fun setFireBaseListener(){
        userSettingsViewModel.getCurrentUser().observe(this, Observer {
            if (it.result == "OK"){
                if (!CurrentUser.image_url!!.equals("default")){
                    Picasso.get().load(CurrentUser.image_url.toString())
                        .placeholder(R.drawable.profile_img)
                        .into(profile_image, object: com.squareup.picasso.Callback{
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                Log.d("picasso","Error loading image ${e!!.message.toString()}")
                            }
                        })

                }
            }else{
                Log.d("setFireBaeListener Error", "Error = ${it.result}")
                Toast.makeText(mainActivity!!.applicationContext,
                    "Error getting user info. Error=${it.result}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUIListeners() {
        settingChangeImageBTN.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri? = data!!.data

            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(activity as MainActivity)

            progressBar.visibility = View.VISIBLE
        }
        sharedViewModel.cropeActivityResult.observe(this, Observer {
            val result = it

            if (1 == 1) {
                val resultUri = result.uri
                var thumbFile = File(resultUri.path.toString())

                // smaller version of the image
                var thumbBitmap = Compressor(activity!!.applicationContext)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                // we upload images to Firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArray)
                var thumbByteArray = byteArray.toByteArray()

                userSettingsViewModel.uploadUserProfile(resultUri,thumbByteArray).observe(this, Observer {
                    if (it.result == "OK"){
                        Toast.makeText(mainActivity, "Your User Image Was Updated",
                            Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(mainActivity, "Error Updateing Image. Error = ${it.result}",
                            Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }
}

package UI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.giladdev.wowchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File

class SettingsActivity : AppCompatActivity() {

    lateinit var mDataBase : DatabaseReference
    lateinit var mCurrentBaseUser: FirebaseUser
    lateinit var mStorageRef: StorageReference
    var GALLERY_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mDataBase = FirebaseDatabase.getInstance().reference
        mCurrentBaseUser = FirebaseAuth.getInstance().currentUser!!
        mStorageRef = FirebaseStorage.getInstance().reference

        var userID = mCurrentBaseUser.uid

        mDataBase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userID)

        mDataBase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapShot: DataSnapshot) {
                var displayName = dataSnapShot.child("display name").value
                var image = dataSnapShot.child("image").value
                var status = dataSnapShot.child("status").value
                var thumbnail = dataSnapShot.child("thumb_image").value

                statusTVID.text = status.toString()
                displayNameTVID.text = displayName.toString()

                if (!image!!.equals("default")){
                    Picasso.get().load(image.toString())
                        .placeholder(R.drawable.profile_img)
                        .into(profile_image)
                }
            }

            override fun onCancelled(dataBaseError: DatabaseError) {
                Log.d("onCancelled","Cancelled")
            }
        })

        changeStatusId.setOnClickListener {
            var intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", statusTVID.text.toString().trim())
            startActivity(intent)
        }
        settingChangeImageBTN.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent,"SELECT_IMAGE"),GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri? = data!!.data

            CropImage.activity(image)
                .setAspectRatio(1,1)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK){
                val resultUri = result.uri
                var userId = mCurrentBaseUser!!.uid
                var thumbFile = File(resultUri.path)

                // smaller version of the image
                var thumbBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                // we upload images to Firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArray)
                var thumbByteArray = byteArray.toByteArray()
                var filePath = mStorageRef!!.child("chat_profile_images")
                    .child(userId+".jpg")

                // create another directory for thumbImages ( smallaer compressed images)
                var thumbFilePath = mStorageRef!!.child("chat_profile_images")
                    .child("thumbs  ")
                    .child(userId+".jpg")

                filePath.putFile(resultUri)
                    .addOnCompleteListener {
                        var downloadUrl : String?= null
                        if (it.isSuccessful) {
                            filePath.downloadUrl.addOnCompleteListener {
                                if (it.isSuccessful){
                                    downloadUrl = it.result.toString()
                                }
                            }
                        }

                        // upload task
                        var uploadTask : UploadTask = thumbFilePath
                            .putBytes(thumbByteArray)
                        uploadTask.addOnCompleteListener {
                            var thumbUrl : String?=null
                            // it.result!!.storage.downloadUrl.toString()
                            if (it.isSuccessful){
                                it.result!!.storage.downloadUrl.addOnCompleteListener {
                                    thumbUrl = it.result.toString()
                                    var updateObj = HashMap<String, Any>()
                                    updateObj.put("image",downloadUrl.toString())
                                    updateObj.put("thumb_image",thumbUrl.toString())
                                    // save the profile image
                                    mDataBase!!.updateChildren(updateObj)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful){
                                                Toast.makeText(this, "Profile Image Saved!",
                                                    Toast.LENGTH_LONG)
                                                    .show()
                                            }else{

                                            }
                                        }
                                }
                            }else{
                        }
                    }

                    }
            }

        }
    }
//        super.onActivityResult(requestCode, resultCode, data)
}


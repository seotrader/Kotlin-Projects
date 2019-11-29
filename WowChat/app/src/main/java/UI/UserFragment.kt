package UI


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser

import com.giladdev.wowchat.R
import com.giladdev.wowchat.adaptors.UsersAdaptor
import com.giladdev.wowchat.models.Users
import com.google.android.gms.common.data.DataBuffer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    lateinit var mUserDatabase : DatabaseReference
    lateinit var  userAdaper : UsersAdaptor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linerLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")


        var options : FirebaseRecyclerOptions<Users> =
            FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUserDatabase, SnapshotParser {
                    Users(it.child("display name").value.toString(),
                        it.child("image").value.toString(),
                        it.child("thumb_image").value.toString(),
                        it.child("status").value.toString())

                }).build()

        usersRecyclerView.setHasFixedSize(true)
        usersRecyclerView.layoutManager = linerLayoutManager
        userAdaper = UsersAdaptor(mUserDatabase,context!!,options)
        usersRecyclerView.adapter = userAdaper
    }

    override fun onStart() {
        super.onStart()
        userAdaper.startListening()
    }

    override fun onStop() {
        super.onStop()
        userAdaper.stopListening()
    }
}

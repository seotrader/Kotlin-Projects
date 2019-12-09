package com.almitasoft.wowchat.UI.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.almitasoft.wowchat.R
import com.almitasoft.wowchat.UI.adaptors.ChatsAdaptor
import com.almitasoft.wowchat.models.FriendlyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chats.*

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {

    var mUserDatabase : DatabaseReference?=null
    var currentUser : FirebaseUser?= null

    var chatsList = arrayListOf<FriendlyMessage>()
    var chatHash = hashMapOf<String,FriendlyMessage>()

    lateinit var chatsAdapter : ChatsAdaptor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onStop() {
        chatsList.clear()
        super.onStop()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsAdapter = ChatsAdaptor(chatsList, context!!)
        chatsRecyclerView.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let{

            mUserDatabase = FirebaseDatabase.getInstance().reference
                .child("messages")
                .child(it.uid)


            mUserDatabase!!.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("FireBase onCancelled","Error = ${p0.message}")

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var lastUserID : String = ""

                    p0.children.forEach {snapShort->
                        var dest_id = snapShort.child("dest_id").value.toString()
                        var from = snapShort.child("from").value.toString()
                        var to = snapShort.child("to").value.toString()
                        var id = snapShort.child("id").value.toString()
                        var text = snapShort.child("text").value.toString()

                        if  (id != currentUser!!.uid){
                            lastUserID = id
                        }

                        if (id == currentUser!!.uid)
                        {
                            from = to
                        }

                        var message: FriendlyMessage = FriendlyMessage(
                            lastUserID, dest_id, text, from, to
                        )

                        message.id = dest_id.replace(currentUser!!.uid,"")

                        if (message.id == "")
                            message.id = id

                        chatHash[dest_id] = message
                    }

                    chatHash.forEach { _, u ->
                        chatsList.add(u)
                    }

                    chatsAdapter.notifyDataSetChanged()
                }
            })
        }
    }
}

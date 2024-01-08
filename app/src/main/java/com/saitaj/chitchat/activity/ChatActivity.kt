package com.saitaj.chitchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.saitaj.chitchat.R
import com.saitaj.chitchat.adapter.MessageAdapter
import com.saitaj.chitchat.databinding.ActivityChatBinding
import com.saitaj.chitchat.model.MessageModel
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderuid : String
    private lateinit var recieveruid : String
    private lateinit var senderRoom : String
    private lateinit var recieverRoom : String
    private lateinit var list: ArrayList<MessageModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)
        senderuid = FirebaseAuth.getInstance().uid.toString()
        recieveruid = intent.getStringExtra("uid")!!
        list = ArrayList()
        senderRoom = senderuid+recieveruid
        recieverRoom = recieveruid+senderuid

        database = FirebaseDatabase.getInstance()
        binding.imageView.setOnClickListener {
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this,"Please enter your message",Toast.LENGTH_SHORT).show()

            }else{
                val message  = MessageModel(binding.messageBox.text.toString(),senderuid,Date().time)

                val randomKey = database.reference.push().key
                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message).addOnSuccessListener {
                        database.reference.child("chats").child(recieverRoom).child("message")
                            .child(randomKey!!).setValue(message).addOnSuccessListener {
                                binding.messageBox.text = null
                                Toast.makeText(this,"message send!!",Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        }
        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for(snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recylerView.adapter = MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"Error: $error",Toast.LENGTH_SHORT).show()
                }

            })
    }
}
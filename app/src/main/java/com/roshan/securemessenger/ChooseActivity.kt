package com.roshan.securemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChooseActivity : AppCompatActivity() {


        private lateinit var chatRecyclerView: RecyclerView
        private lateinit var messageBox: EditText
        lateinit var sendButton: ImageView
        private lateinit var messageAdapter: MessageAdapter
        private lateinit var messageList : ArrayList<Message>
        private lateinit var mDbRef : DatabaseReference
        private lateinit var encryptedMsgIcon : ImageView

        var receiverRoom: String? = null
        var senderRoom: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_choose)

            encryptedMsgIcon = findViewById(R.id.encrypted_msg_icon)


            val name = intent.getStringExtra("name")
            val receiverUid = intent.getStringExtra("uid")

            val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            mDbRef = FirebaseDatabase.getInstance().getReference()

            senderRoom = receiverUid + senderUid
            receiverRoom = senderUid + receiverUid

            supportActionBar?.title = name

            chatRecyclerView = findViewById(R.id.chatRecyclerView)
            messageBox = findViewById(R.id.messageBox)
            sendButton = findViewById(R.id.sendButton)
            messageList = ArrayList()
            messageAdapter = MessageAdapter(this,messageList)

            chatRecyclerView.layoutManager = LinearLayoutManager(this)
            chatRecyclerView.adapter = messageAdapter

            // logic for adding data to recycler view
            mDbRef.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        messageList.clear()
                        for(postSnapshot in snapshot.children){

                            val message = postSnapshot.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                        messageAdapter.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            sendButton.setOnClickListener{
                // first send message to the database
                val message = messageBox.text.toString()
                val messageObject = Message(message,senderUid.toString())

                mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)
                }
                messageBox.setText("")

            }

            encryptedMsgIcon.setOnClickListener{
                val message = messageBox?.text.toString()!!
                val intent = Intent(this,AES::class.java)
                intent.putExtra("plaintext",message)
                startActivity(intent)
            }

        }
    }
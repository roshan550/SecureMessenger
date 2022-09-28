package com.roshan.securemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class AES : AppCompatActivity() {
    lateinit var plainText: TextView
    lateinit var cipherText: TextView
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aes)

        plainText = findViewById(R.id.plain_text)
        cipherText = findViewById(R.id.cipher_text)
        mDbRef = FirebaseDatabase.getInstance().reference


        val message = intent.getStringExtra("plaintext")
        plainText.text = message.toString()


        val plaintext: ByteArray = message?.toByteArray()!!
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv

        cipherText.text = ciphertext.toString()


    }
}

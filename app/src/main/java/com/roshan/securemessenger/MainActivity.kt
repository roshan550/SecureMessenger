package com.roshan.securemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import java.util.logging.Handler


class MainActivity : AppCompatActivity() {
    private lateinit var imgLogo:ImageView
    private lateinit var txtApp:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgLogo=findViewById(R.id.imgLogo)
        txtApp=findViewById(R.id.txtApp)

        android.os.Handler().postDelayed({
            val intent = Intent(this,LoginActivity::class.java)
            finish()
            startActivity(intent)
        },2000)
    }
}
package com.example.focus_app

import ApiRequest.GetCategory
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var messageTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        messageTextView = findViewById(R.id.textView)

        val requestBody=mapOf(
            "login" to "tehst",
            "email" to "tejst",
            "password" to "thest"
        )

        val request=mapOf(
            "name" to "slkd",
            "cat_name" to "sport",
            "user_email" to "test"
        )
        //create and send user for backend
        //ConnectServer(messageTextView)
        //CreateUser(requestBody,messageTextView)
         GetCategory(messageTextView)
       // Authorization(requestBody,messageTextView)
    }
}
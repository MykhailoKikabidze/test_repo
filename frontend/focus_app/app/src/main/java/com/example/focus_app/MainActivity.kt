package com.example.focus_app

import ApiRequest.UpdateDailyPoints
import Data.getSavedUserEmail
import SignUpPage.SignUpPage
import TimerPage.TimerPage
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity(){

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = dateFormat.format(currentDate)
        UpdateDailyPoints("test",date) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }

        val userEmail= getSavedUserEmail(this)
        if(savedInstanceState==null && userEmail==null)
        {
            intent= Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }
        else
        {
            intent= Intent(this,TimerPage::class.java)
            startActivity(intent)
        }
    }

}
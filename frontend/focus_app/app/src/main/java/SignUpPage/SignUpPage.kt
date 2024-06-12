package SignUpPage

import ApiRequest.CreateUser
import ApiRequest.UpdateDailyPoints
import ApiRequest.rightAutorization
import Data.SaveUserEmail
import LoginPage.LoginPage
import StatisticCharts.PieChart
import TimerPage.TimerPage
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.focus_app.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date


class SignUpPage : AppCompatActivity(){


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_sign_up_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_sing_up)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val email = findViewById<TextInputEditText>(R.id.email_sign_up)

        val login = findViewById<TextInputEditText>(R.id.login_sign_up)

        val password = findViewById<TextInputEditText>(R.id.repassword_sign_up)
        val changeToLogin=findViewById<TextView>(R.id.textview_refer_sign_up)

        val singUpButton : Button =findViewById(R.id.btn_sign_up)
        singUpButton.setOnClickListener{
            val user= mapOf(
                "login" to "${login.text}",
                "email" to "${email.text}",
                "password" to "${password.text}"
            )

//            if(isEmailValid(email.text.toString()) && isPasswordValid(password.text.toString())
//                && isUserNameValid(login.text.toString()))
//            {
                CreateUser(user){ result ->
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    if(result==rightAutorization){
                        SaveUserEmail(this,email.text.toString())
                        val currentDate = Date()
                        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                        val date = dateFormat.format(currentDate)
                        UpdateDailyPoints(email.text.toString(),date){ status->
                            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                        }
                        val intent = Intent(this, TimerPage::class.java)
                        startActivity(intent)
                    }
                }
//            }
        }




        changeToLogin.setOnClickListener{
            val intent= Intent(this,LoginPage::class.java)
            startActivity(intent)
        }
    }

}
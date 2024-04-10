package SignUpPage

import ApiRequest.CreateUser
import LoginPage.LoginPage
import TimerPage.TimerPage
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.focus_app.MainActivity
import com.example.focus_app.R
import com.google.android.material.textfield.TextInputEditText


class SignUpPage : AppCompatActivity() {
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
                "login" to "$login",
                "email" to "$email",
                "password" to "$password"
            )

            CreateUser(user){ result ->

                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TimerPage::class.java)
                startActivity(intent)

            }
            val intent=Intent(this, TimerPage::class.java)
            startActivity(intent)
        }

        changeToLogin.setOnClickListener{
            val intent= Intent(this,LoginPage::class.java)
            startActivity(intent)
        }
    }
}
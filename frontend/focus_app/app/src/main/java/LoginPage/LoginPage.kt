package LoginPage

import ApiRequest.Authorization
import ApiRequest.rightAutorization
import CheckUserData.isEmailValid
import CheckUserData.isPasswordValid
import TimerPage.TimerPage
import android.annotation.SuppressLint
import android.os.Bundle
import com.example.focus_app.R
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class LoginPage : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logInButton: Button=findViewById(R.id.btn_login)
        val email = findViewById<TextInputEditText>(R.id.et_email_login)
        val password = findViewById<TextInputEditText>(R.id.password_login)
        val changeToSingUp:TextView=findViewById(R.id.textview_refer_login)
        logInButton.setOnClickListener{
            val user= mapOf(
                "login" to "",
                "email" to "${email.text}",
                "password" to "${password.text}"
            )

//            if(isEmailValid(email.text.toString()) && isPasswordValid(password.text.toString()))
//            {
                Authorization(user) { result ->
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    if(result== rightAutorization) {
                        val intent = Intent(this, TimerPage::class.java)
                        startActivity(intent)
                    }
//                }
            }


        }

//        changeToSingUp.setOnClickListener{
//            val intent= Intent(this,SignUpPage::class.java)
//            startActivity(intent)
//        }
    }

}

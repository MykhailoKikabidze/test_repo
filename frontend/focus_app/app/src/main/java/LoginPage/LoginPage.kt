package LoginPage

import ApiRequest.CreateUser
import SignUpPage.SignUpPage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.focus_app.R
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText

class LoginPage : AppCompatActivity() {

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
                "email" to "$email",
                "password" to "$password"
            )

            CreateUser(user,this)
        }

        changeToSingUp.setOnClickListener{
            val intent= Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }
    }
    private fun switchToFragment(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        // Perform a fragment transaction to replace the current fragment with the new one
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }
}

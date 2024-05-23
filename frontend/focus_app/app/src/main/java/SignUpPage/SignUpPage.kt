package SignUpPage

import ApiRequest.CreateUser
import ApiRequest.rightAutorization
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


class SignUpPage : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

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
                        val intent = Intent(this, TimerPage::class.java)
                        startActivity(intent)
                    }
                }
//            }
        }



        drawerLayout=findViewById(R.id.toolbar_sign_up_main)

        val navigationView=findViewById<NavigationView>(R.id.nav_view_sign_up)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_sign_up)
        val toggle= ActionBarDrawerToggle(this,drawerLayout, toolbar ,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        changeToLogin.setOnClickListener{
            val intent= Intent(this,LoginPage::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_menu -> {
                Toast.makeText(this, "wowo", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.settings_menu -> {
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.statistics_menu -> {
                var intent=Intent(this, PieChart::class.java)
                startActivity(intent)
                return true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed(){
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
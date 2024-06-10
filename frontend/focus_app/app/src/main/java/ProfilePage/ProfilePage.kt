package ProfilePage

import ApiRequest.RetrofitClient
import Data.userEmail
import StatisticCharts.PieChart
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.focus_app.R
import com.google.android.material.navigation.NavigationView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfilePage : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    private lateinit var currentUsernameTextView: TextView
    private lateinit var currentUsername1TextView: TextView
    private lateinit var currentUserEmailTextView: TextView
    private lateinit var currentPasswordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentUsernameTextView = findViewById(R.id.currentUsername)
        currentUsername1TextView = findViewById(R.id.currentUsername1)
        currentUserEmailTextView = findViewById(R.id.currentUserEmail)
        currentPasswordTextView = findViewById(R.id.currentPassword)


        drawerLayout=findViewById(R.id.toolbar_profile_main)

        val navigationView=findViewById<NavigationView>(R.id.nav_view_profile)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_profile)
        val toggle= ActionBarDrawerToggle(this,drawerLayout, toolbar ,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<TextView>(R.id.changeUsernameButton).setOnClickListener {
            showChangeDialog("Username")
        }

        findViewById<TextView>(R.id.changeUserEmailButton).setOnClickListener {
            showChangeDialog("User Email")
        }

        findViewById<TextView>(R.id.changePasswordButton).setOnClickListener {
            showChangeDialog("Password")
        }

        // Загружаем текущие данные пользователя
        loadUserData()
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val currentUsername = sharedPref.getString("username", "Username not found")
        val currentUsername1 = sharedPref.getString("username", "Username not found")
        val currentEmail = sharedPref.getString("email", "Email not found")
        val currentPassword = sharedPref.getString("password", "********")

        // Обновляем TextView текущими данными
        currentUsernameTextView.text = currentUsername
        currentUsername1TextView.text = currentUsername1
        currentUserEmailTextView.text = currentEmail
        currentPasswordTextView.text = currentPassword?.replace(Regex("."), "*")
    }

    private fun showChangeDialog(field: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_change_field, null)
        dialogBuilder.setView(dialogView)

        val editText = dialogView.findViewById<EditText>(R.id.editText)
        dialogBuilder.setTitle("Change $field")
        dialogBuilder.setMessage("Enter new $field")
        dialogBuilder.setPositiveButton("Change") { _, _ ->
            val newValue = editText.text.toString()
            updateField(field, newValue)
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun updateField(field: String, newValue: String) {
        when (field) {
            "Username" -> updateUsername(newValue)
            "User Email" -> updateUserEmail(newValue)
            "Password" -> updatePassword(newValue)
        }
    }

    private fun updateUsername(newUsername: String) {
        // Вызов функции для обновления имени пользователя в базе данных
        RetrofitClient.instance.updateUserLogin(userEmail, newUsername).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    currentUsernameTextView.text = newUsername
                    currentUsername1TextView.text = newUsername
                    saveUserData("username", newUsername)
                    Toast.makeText(this@ProfilePage, "Username updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfilePage, "Failed to update username", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@ProfilePage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserEmail(newUserEmail: String) {
        // Вызов функции для обновления электронной почты пользователя в базе данных
        RetrofitClient.instance.updateUserEmail(userEmail, newUserEmail).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    currentUserEmailTextView.text = newUserEmail
                    saveUserData("email", newUserEmail)
                    Toast.makeText(this@ProfilePage, "User email updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfilePage, "Failed to update user email", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@ProfilePage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePassword(newPassword: String) {
        // Вызов функции для обновления пароля пользователя в базе данных
        RetrofitClient.instance.updateUserPassword(userEmail, newPassword).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    currentPasswordTextView.text = newPassword.replace(Regex("."), "*")
                    saveUserData("password", newPassword)
                    Toast.makeText(this@ProfilePage, "Password updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfilePage, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@ProfilePage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData(key: String, value: String) {
        val sharedPref = getSharedPreferences("userData", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.statistics_menu -> {
                val intent=Intent(this, PieChart::class.java)
                startActivity(intent)
                return true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

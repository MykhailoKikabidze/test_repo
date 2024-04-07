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
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText

class LoginPage : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflater=LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.fragment_login_page, container, false) //this interface is from fragment_home

        val changeToSingUp=view.findViewById<TextView>(R.id.textview_refer_login)

        val logInButton: Button=view.findViewById(R.id.btn_login)
        val email = view.findViewById<TextInputEditText>(R.id.et_email_login)
        val password = view.findViewById<TextInputEditText>(R.id.password_login)

        logInButton.setOnClickListener{
            val user= mapOf(
                "login" to "",
                "email" to "$email",
                "password" to "$password"
            )

            CreateUser(user,requireContext())
        }

        changeToSingUp.setOnClickListener{
            switchToFragment(SignUpPage(), requireFragmentManager(), R.id.main_login)
        }

        return view
    }
    private fun switchToFragment(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        // Perform a fragment transaction to replace the current fragment with the new one
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }
}

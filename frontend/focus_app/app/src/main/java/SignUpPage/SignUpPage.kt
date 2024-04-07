package SignUpPage

import ApiRequest.CreateUser
import LoginPage.LoginPage
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.focus_app.R
import com.google.android.material.textfield.TextInputEditText


class SignUpPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflater= LayoutInflater.from(requireContext())
        val view= inflater.inflate(R.layout.fragment_sign_up_page, container, false)

        val email = view.findViewById<TextInputEditText>(R.id.email_sign_up)

        val login = view.findViewById<TextInputEditText>(R.id.login_sign_up)

        val password = view.findViewById<TextInputEditText>(R.id.repassword_sign_up)
        val changeToLogin=view.findViewById<TextView>(R.id.textview_refer_sign_up)

        val singUpButton : Button =view.findViewById(R.id.btn_sign_up)
        singUpButton.setOnClickListener{
            val user= mapOf(
                "login" to "$login",
                "email" to "$email",
                "password" to "$password"
            )

            CreateUser(user,requireContext())
        }

        changeToLogin.setOnClickListener{
            switchToFragment(LoginPage(), requireFragmentManager(), R.id.main_sing_up)
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
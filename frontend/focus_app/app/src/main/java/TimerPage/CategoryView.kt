package TimerPage

import ApiRequest.GetCategory
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.focus_app.R


class CategoryView : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.category_view_timer, container, false)
        val listOfCategories=view.findViewById<ListView>(R.id.list_of_category)
        GetCategory(listOfCategories){result->
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
        }
        return view
    }

}


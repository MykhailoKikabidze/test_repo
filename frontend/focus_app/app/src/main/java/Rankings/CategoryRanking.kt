package Rankings

import ApiRequest.GetCategory
import Rankings.Rankings
import TimerPage.ActivityView
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.focus_app.R

class CategoryRanking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.category_view_timer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.category_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listOfCategories=findViewById<ListView>(R.id.list_of_category)
        GetCategory(listOfCategories){result->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }

        listOfCategories.setOnItemClickListener { parent, view, position, id ->
            val selectedCategory = parent.getItemAtPosition(position) as String
            val intent = Intent(this, Rankings::class.java)
            intent.putExtra("category", selectedCategory)

            startActivity(intent)
        }
    }
}
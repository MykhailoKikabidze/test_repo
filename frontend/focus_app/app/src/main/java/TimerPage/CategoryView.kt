package TimerPage

import ApiRequest.GetCategory
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.focus_app.R

class CategoryView : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.category_view_timer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_category_timer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val listOfCategories=findViewById<ListView>(R.id.list_of_category)
        GetCategory(this,listOfCategories)

    }
}
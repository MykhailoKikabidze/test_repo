package TimerPage

import ApiRequest.GetCategory
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.focus_app.R
import ApiRequest.CreateActivity
import ApiRequest.UpdateActivity
import ApiRequest.DeleteActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ActivityView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_view)

        val rootView = findViewById<View>(android.R.id.content)
        rootView.setBackgroundResource(R.drawable.timer_img)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        val editTextActivityName = findViewById<EditText>(R.id.editTextActivityName)
        val listViewActivities = findViewById<ListView>(R.id.listViewActivities)

        val category = intent.getStringExtra("category") ?: ""

        // Button Add click
        buttonAdd.setOnClickListener {
            val activityName = editTextActivityName.text.toString()
            val requestBody = mapOf("name" to activityName)



            val cat_name = "sport"
            val user_email = "test"
            CreateActivity(requestBody, cat_name, user_email) { result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

                val currentActivityList = mutableListOf<String>()
                for (i in 0 until listViewActivities.count) {
                    currentActivityList.add(listViewActivities.getItemAtPosition(i).toString())
                }

                currentActivityList.add(activityName)

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currentActivityList)
                
                listViewActivities.adapter = adapter
            }

        }

        // Button Delete click
        buttonDelete.setOnClickListener {
            val selectedItem = listViewActivities.selectedItem.toString()
            val activity_name = selectedItem.toString()

            val cat_name = "sport"
            val user_email = "test"

            DeleteActivity(activity_name, cat_name, user_email) { result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            }
        }

        //Buton Update click
        buttonUpdate.setOnClickListener {
            val selectedActivity = listViewActivities.selectedItem.toString()
            val newActivityName = editTextActivityName.text.toString()
            val requestBody = mapOf("new_activity_name" to newActivityName)

            val catName = "sport"
            val userEmail = "test"

            UpdateActivity(requestBody, catName, userEmail, selectedActivity) { result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            }
        }


    }
}






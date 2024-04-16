package TimerPage

import ApiRequest.CreateActivity
import ApiRequest.DeleteActivity
import ApiRequest.GetActivities
import ApiRequest.UpdateActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.focus_app.R


class ActivityView : AppCompatActivity() {
    private var selectedActivity: String? = null

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
        val cat_name = "sport"
        val user_email = "test"
        GetActivities(listViewActivities, cat_name, user_email) { result ->
            val resultString = result.toString()
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
        }

        // Обработчик нажатия на элемент списка
        listViewActivities.setOnItemClickListener { parent, view, position, id ->
            // Получаем выбранный элемент списка
            selectedActivity = listViewActivities.getItemAtPosition(position) as String
            // Опционально: отобразить выбранный элемент пользователю или выполнить другие действия
        }

        // Button Add click
        buttonAdd.setOnClickListener {
            val activityName = editTextActivityName.text.toString()
            val requestBody = mapOf("name" to activityName)

            val cat_name = "sport"
            val user_email = "test"

            CreateActivity(requestBody, cat_name, user_email) { result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()

                // Получаем список активностей после добавления новой активности
                GetActivities(listViewActivities, cat_name, user_email) { result ->
                    val resultString = result.toString()
                    Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Button Delete click
        buttonDelete.setOnClickListener {
            // Проверяем, что есть выбранный элемент для удаления
            selectedActivity?.let { activity ->
                val cat_name = "sport"
                val user_email = "test"

                // Выполняем удаление выбранного элемента
                DeleteActivity(activity, cat_name, user_email) { result ->
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    // Обновление списка активностей после удаления
                    GetActivities(listViewActivities, cat_name, user_email) { result ->
                        val resultString = result.toString()
                        Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(this, "Выберите активность для удаления", Toast.LENGTH_SHORT).show()
            }
        }
        // Button Update click
        buttonUpdate.setOnClickListener {
            val selectedActivity = listViewActivities.selectedItem.toString()
            val newActivityName = editTextActivityName.text.toString()
            val requestBody = mapOf("new_activity_name" to newActivityName)

            val catName = "sport"
            val userEmail = "test"

            UpdateActivity(requestBody, catName, userEmail, selectedActivity) { result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                // Обновление списка активностей после обновления
                GetActivities(listViewActivities, catName, userEmail) { result ->
                    val resultString = result.toString()
                    Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
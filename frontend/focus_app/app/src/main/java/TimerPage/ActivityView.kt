package TimerPage

import ActivitiesAdapter
import ApiRequest.CreateActivity
import ApiRequest.DeleteActivity
import ApiRequest.GetActivities
import ApiRequest.UpdateActivity
import Data.activityName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R



class ActivityView : AppCompatActivity() {
    private var selectedActivity: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_view)

        val rootView = findViewById<View>(android.R.id.content)
        rootView.setBackgroundResource(R.drawable.timer_img)

        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val editTextActivityName = findViewById<EditText>(R.id.editTextActivityName)

        recyclerView = findViewById(R.id.recyclerViewActivities)
        adapter = ActivitiesAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val joj=intent.extras?.getString("category")?:""


        setupSwipeToDelete()
        setupLongClick()

        buttonAdd.setOnClickListener {
            val activityName = editTextActivityName.text.toString().trim()
            if (activityName.isNotEmpty()) {
                CreateActivity(mapOf("name" to activityName), "sport", "test") { result ->
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    adapter.addActivity(activityName)
                    editTextActivityName.setText("")  // Очистка поля после добавления
                }
            } else {
                Toast.makeText(this, "Please enter a valid activity name.", Toast.LENGTH_SHORT).show()
            }
            val intent=Intent(this,TimerPage::class.java)
            intent.putExtra("category",joj)
            startActivity(intent)

        }

        editTextActivityName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buttonAdd.performClick()
                true
            } else {
                false
            }
        }

        GetActivities(recyclerView, "sport", "test") { activities ->
            adapter.activities
        }
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val activityName = adapter.getActivityAtPosition(position)
                DeleteActivity(activityName, "sport", "test") { result ->
                    Toast.makeText(this@ActivityView, result, Toast.LENGTH_SHORT).show()
                    adapter.removeItem(position)
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    private fun setupLongClick() {
        adapter.onItemLongClicked = { activityName ->
            val editText = EditText(this)
            editText.setText(activityName)
            editText.selectAll()

            AlertDialog.Builder(this)
                .setTitle("Edit Activity")
                .setView(editText)
                .setPositiveButton("Save") { dialog, which ->
                    val newName = editText.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        val position = adapter.activities.indexOf(activityName)
                        if (position != -1) {
                            UpdateActivity(mapOf("new_activity_name" to newName), "sport", "test", activityName) { result ->
                                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                                adapter.updateActivity(position, newName)
                            }
                        }
                    } else {
                        Toast.makeText(this, "Activity name cannot be empty.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}

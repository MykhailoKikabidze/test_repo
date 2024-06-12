
import TimerPage.TimerPage
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActivitiesAdapter(var activities: MutableList<String>,
                        private val category: String) : RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>() {

    var onItemLongClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ActivityViewHolder(itemView,category)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.textView.text = activity
        holder.itemView.setOnLongClickListener {
            onItemLongClicked?.invoke(activity)
            true
        }
    }

    class ActivityViewHolder(itemView: View, private val category: String) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)

        init {
            itemView.setOnClickListener {
                val activityName = textView.text.toString()
                val intent = Intent(itemView.context, TimerPage::class.java) // Replace with your target activity class
                intent.putExtra("activity", activityName)
                intent.putExtra("category", category)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = activities.size

    fun addActivity(activityName: String) {
        activities.add(activityName)
        notifyItemInserted(activities.size - 1)
    }

    fun removeItem(position: Int) {
        activities.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateActivities(newActivities: List<String>) {
        activities.clear()
        activities.addAll(newActivities)
        notifyDataSetChanged()
    }

    fun updateActivity(position: Int, newName: String) {
        activities[position] = newName
        notifyItemChanged(position)
    }

    fun getActivityAtPosition(position: Int): String = activities[position]
}

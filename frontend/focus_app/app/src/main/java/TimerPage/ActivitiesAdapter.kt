
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActivitiesAdapter(var activities: MutableList<String>) : RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder>() {

    var onItemLongClicked: ((String) -> Unit)? = null

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ActivityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.textView.text = activity
        holder.itemView.setOnLongClickListener {
            onItemLongClicked?.invoke(activity)
            true
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

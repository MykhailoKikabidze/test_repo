package FriendsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R

class FriendsAdapter(
    private val context: Context,
    private val users: List<String>,
    private val friends: List<String>,
    private val onAdd: (String) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_USER = 0
    private val TYPE_FRIEND = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < users.size) TYPE_USER else TYPE_FRIEND
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == TYPE_USER) {
            UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
        } else {
            FriendViewHolder(inflater.inflate(R.layout.item_friend, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_USER) {
            (holder as UserViewHolder).bind(users[position])
        } else {
            (holder as FriendViewHolder).bind(friends[position - users.size])
        }
    }

    override fun getItemCount(): Int = users.size + friends.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emailText: TextView = itemView.findViewById(R.id.emailText)
        private val addButton: Button = itemView.findViewById(R.id.addButton)

        fun bind(email: String) {
            emailText.text = email
            addButton.setOnClickListener { onAdd(email) }
        }
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emailText: TextView = itemView.findViewById(R.id.emailText)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(email: String) {
            emailText.text = email
            deleteButton.setOnClickListener { onDelete(email) }
        }
    }
}

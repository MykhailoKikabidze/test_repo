package FriendsPage

import FriendProfile.FriendProfile
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R

class FriendsAdapter(private val onRemoveClick: ((String) -> Unit)? = null) : ListAdapter<String, FriendsAdapter.FriendViewHolder>(FriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view, onRemoveClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friendEmail = getItem(position)
        holder.bind(friendEmail)
    }

    class FriendViewHolder(itemView: View, private val onRemoveClick: ((String) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        private val textViewFriendEmail: TextView = itemView.findViewById(R.id.textViewFriendEmail)


        fun bind(friendEmail: String) {
            textViewFriendEmail.text = friendEmail
            itemView.setOnClickListener {
                // Start the FriendProfile activity
                val context = itemView.context
                val intent = Intent(context, FriendProfile::class.java)
                intent.putExtra("email", friendEmail)
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                onRemoveClick?.invoke(friendEmail)
                true
            }
//
//        fun bind(friendEmail: String) {
//            textViewFriendEmail.text = friendEmail
//            itemView.setOnClickListener {
//                onRemoveClick?.invoke(friendEmail)
//            }
        }
    }



    class FriendDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}

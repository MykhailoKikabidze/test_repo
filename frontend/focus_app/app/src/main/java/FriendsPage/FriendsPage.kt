package FriendsPage

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R
import ApiRequest.*
import com.google.gson.Gson
import Data.getSavedUserEmail

class FriendsPage : AppCompatActivity() {
    private lateinit var friendsAdapter: FriendsAdapter
    private var friendsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_page)

        val recyclerViewFriends: RecyclerView = findViewById(R.id.recyclerViewFriends)
        recyclerViewFriends.layoutManager = LinearLayoutManager(this)
        friendsAdapter = FriendsAdapter(this, emptyList(), friendsList, {}, ::deleteFriend)
        recyclerViewFriends.adapter = friendsAdapter

        val searchView: SearchView = findViewById(R.id.searchViewFriends)
        searchView.setOnSearchClickListener {
            showSearchDialog()
        }

        updateFriendsList()  // Загружаем список друзей при старте
    }

    private fun showSearchDialog() {
        val dialog = SearchUsersDialogFragment(::addFriend)
        dialog.show(supportFragmentManager, "SearchUsersDialogFragment")
    }

    private fun addFriend(email: String) {
        val currentUserEmail = getSavedUserEmail(this)
        if (currentUserEmail != null) {
            Log.d("FriendsPage", "Adding friend: $email for user: $currentUserEmail")
            AddFriend(currentUserEmail, email) { response ->
                Log.d("FriendsPage", "Add friend response: $response")
                if (response.contains("Success")) {
                    Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show()
                    updateFriendsList()
                } else {
                    Toast.makeText(this, "Failed to add friend", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Failed to get current user email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFriend(email: String) {
        val currentUserEmail = getSavedUserEmail(this)
        if (currentUserEmail != null) {
            Log.d("FriendsPage", "Deleting friend: $email for user: $currentUserEmail")
            DeleteFriend(currentUserEmail, email) { response ->
                Log.d("FriendsPage", "Delete friend response: $response")
                if (response.contains("Success")) {
                    Toast.makeText(this, "Friend deleted successfully", Toast.LENGTH_SHORT).show()
                    updateFriendsList() // Обновляем список друзей сразу после удаления
                } else {
                    Toast.makeText(this, "Failed to delete friend", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Failed to get current user email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFriendsList() {
        val currentUserEmail = getSavedUserEmail(this)
        if (currentUserEmail != null) {
            GetFriends(currentUserEmail) { friends ->
                friendsList.clear()
                friendsList.addAll(friends)
                friendsAdapter.notifyDataSetChanged()
            }
        } else {
            Toast.makeText(this, "Failed to get current user email", Toast.LENGTH_SHORT).show()
        }
    }
}

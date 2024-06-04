package FriendsPage

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R
import ApiRequest.*

class FriendsPage : AppCompatActivity() {
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friends_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_friends)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация RecyclerView и адаптера без функции удаления
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFriends)
        recyclerView.layoutManager = LinearLayoutManager(this)
        friendsAdapter = FriendsAdapter()  // Создание адаптера без функции удаления
        recyclerView.adapter = friendsAdapter



        val searchView: SearchView = findViewById(R.id.searchViewFriends)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchAndAddFriends(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Необходимо, если вы хотите обрабатывать изменения текста в режиме реального времени
                return false
            }
        })
    }

    private fun searchAndAddFriends(email: String) {
        // Функция для поиска и потенциального добавления друзей
        GetFriends(email) { friendsList ->
            if (friendsList.isEmpty()) {
                // Если друзей с таким email нет, предлагаем добавить его
                Toast.makeText(
                    this,
                    "No friends found with this email, adding new friend",
                    Toast.LENGTH_SHORT
                ).show()
                AddFriend(email, email) { response ->
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                    // Повторный поиск для обновления списка после добавления
                    GetFriends(email) { updatedList ->
                        friendsAdapter.submitList(updatedList)
                    }
                }
            } else {
                // Если друзья найдены, отображаем их
                friendsAdapter.submitList(friendsList)
            }
        }
    }

}

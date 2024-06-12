package FriendsPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focus_app.R
import ApiRequest.*
import com.google.gson.Gson

class SearchUsersDialogFragment(private val onAdd: (String) -> Unit) : DialogFragment() {

    private lateinit var searchAdapter: FriendsAdapter
    private var searchResults = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_search_users, container, false)

        val searchView: SearchView = view.findViewById(R.id.searchViewDialog)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewSearchDialog)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = FriendsAdapter(requireContext(), searchResults, emptyList(), { email ->
            onAdd(email)
            dismiss() // Закрыть диалог после добавления друга
        }, {})
        recyclerView.adapter = searchAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    GetUsers { usersJson ->
                        val users = Gson().fromJson(usersJson, Array<String>::class.java).toList()
                        searchResults.clear()
                        searchResults.addAll(users.filter { it.contains(query, ignoreCase = true) })
                        searchAdapter.notifyDataSetChanged()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }
}

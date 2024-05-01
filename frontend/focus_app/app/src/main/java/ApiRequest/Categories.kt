package ApiRequest

import Data.categoriesNames
import android.R
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun GetCategory(listView: ListView, callback: (String) -> Unit) {
    RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val listCategories = response.body() ?: emptyList()
                categoriesNames = listCategories.map { category -> category.name }

                val adapter = ArrayAdapter(listView.context, R.layout.simple_list_item_1, categoriesNames)

                listView.adapter = adapter

                callback("Categories loaded successfully")
            } else {
                callback("Unknown error")
            }
        }

        override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            handler.post {
                callback("Failure: ${t.message}")
            }
        }
    })
}
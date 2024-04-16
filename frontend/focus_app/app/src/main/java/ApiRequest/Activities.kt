package ApiRequest

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.focus_app.R
import kotlin.collections.map
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

fun GetCategory(listView: ListView, callback: (String) -> Unit) {
    RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val listCategories = response.body() ?: emptyList()
                val categoryNames = listCategories.map { category -> category.name }

                // Create an ArrayAdapter with category names
                val adapter = ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, categoryNames)

                // Set the adapter to the ListView
                listView.adapter = adapter

                // Notify the callback that categories have been loaded
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


fun CreateActivity(requestBody: Map<String,String>, cat_name:String, user_email:String, callback: (String) -> Unit) {
    RetrofitClient.instance.createActivity(requestBody, cat_name, user_email).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

                val msg = jsonObj.get("message")

                if (jsonObj.has("status")) {
                    callback(msg.toString())
                } else {
                    callback("Some Error")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                callback("Error: $errorMessage")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure: ${t.message}")
        }
    })
}

fun DeleteActivity(activity_name:String , cat_name: String, user_email: String, callback: (String) -> Unit) {
    RetrofitClient.instance.deleteActivity(activity_name, cat_name, user_email).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)


                if (jsonObj.has("status")) {
                    callback(jsonObj.get("message").toString())
                } else {
                    callback(jsonObj.toString())
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                callback("Error: $errorMessage")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure: ${t.message}")
        }
    })
}

fun UpdateActivity(requestBody: Map<String,String>, cat_name:String, user_email:String,new_activity_name:String, callback: (String) -> Unit) {
    RetrofitClient.instance.updateActivity(requestBody, cat_name, user_email, new_activity_name).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

                if (jsonObj.has("status")) {
                    callback("Updated successfully")
                } else {
                    callback("Some Error")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                callback("Error: $errorMessage")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure: ${t.message}")
        }
    })
}

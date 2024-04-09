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

fun CreateActivity(requestBody: Map<String,String>,cat_name:String,user_email:String,messageTextView: TextView)
{
    RetrofitClient.instance.createActivity(requestBody,cat_name, user_email).enqueue(object : Callback<Any> {
        private val handler = Handler(Looper.getMainLooper())
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)


                if (jsonObj.has("status")) {
                    messageTextView.text = "Already exists"
                } else {
                    messageTextView.text = "Some Error"
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                handler.post {
                    messageTextView.text = "Error: $errorMessage"
                }
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            handler.post {
                messageTextView.text = "Failure: ${t.message}"
            }
        }
    })
}


fun GetCategory(context: Context, listView: ListView) {
    RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val listCategories = response.body()?:  emptyList()
                val categoryNames = listCategories.map { category -> category.name }

                // Display category names
                val categoryNamesText = categoryNames.joinToString(", ")
                val adapter = ArrayAdapter(listView.context, R.layout.category_view_timer, categoryNames)
                listView.adapter = adapter
            } else {
                Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            handler.post {
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        }
    })

    fun DeleteActivity(requestBody: Map<String, String>, cat_name: String, user_email: String, messageTextView: TextView) {
        RetrofitClient.instance.deleteActivity(requestBody, cat_name, user_email)
            .enqueue(object : Callback<Any> {
                private val handler = Handler(Looper.getMainLooper())

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        val gson = Gson()
                        val jsonObject = gson.toJson(response.body())
                        val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

                        if (jsonObj.has("status")) {
                            messageTextView.text = "Already exists"
                        } else {
                            messageTextView.text = jsonObj.toString()
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        handler.post {
                            messageTextView.text = "Error: $errorMessage"
                        }
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    handler.post {
                        messageTextView.text = "Failure: ${t.message}"
                    }
                }
            })
    }

}
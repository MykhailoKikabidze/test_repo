package ApiRequest

import android.os.Handler
import android.os.Looper
import android.widget.TextView
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

fun GetCategory(messageTextView: TextView) {
    RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val listCategories = response.body()?:  emptyList()
                val categoryNames = listCategories.map { category -> category.name }

                // Display category names
                val categoryNamesText = categoryNames.joinToString(", ")
                handler.post {
                    messageTextView.text = categoryNamesText
                }
            } else {
                messageTextView.text="ERROR"
            }
        }

        override fun onFailure(call: Call<List<Category>>, t: Throwable) {
            handler.post {
                messageTextView.text = "Failure: ${t.message}"
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
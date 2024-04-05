package ApiRequest

import android.content.Context
import android.os.Looper
import retrofit2.Callback
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
val rightAutorization="Congratulation?!!!"
val wrongAutorization="You need to log in first"

fun Authorization (requestBody: Map<String, String>, messageTextView: TextView) {
    RetrofitClient.instance.authorization(requestBody).enqueue(object : Callback<Any> {
        private val handler = Handler(Looper.getMainLooper())
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

                if (jsonObj.has("login")) {
                    messageTextView.text = rightAutorization
                } else if (jsonObj.has("num")) {
                    messageTextView.text = wrongAutorization
                } else {
                    messageTextView.text = "UNKNOWN"
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


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



fun Authorization(requestBody: Map<String, String>, callback: (String) -> Unit) {
    RetrofitClient.instance.authorization(requestBody).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

                if (jsonObj.has("login")) {
                    //  showToast(context, "Congratulations")
                    callback(rightAutorization) // Authorization successful
                } else if (jsonObj.has("num")) {
                    //  showToast(context, "You haven't account first sign in")
                    callback(wrongAutorization) // Authorization failed
                } else {
                    //  showToast(context, "Unknown error")
                    callback("Unknown error") // Authorization failed
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                // showToast(context, "Error: $errorMessage")
                callback("Error: $errorMessage") // Authorization failed
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            // showToast(context, "Failure: ${t.message}")
            callback("Failure: ${t.message}") // Authorization failed
        }
    })
}





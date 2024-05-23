package ApiRequest

import android.os.Handler
import android.os.Looper
import retrofit2.Call
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Callback
import retrofit2.Response

fun UpdateUserLogin(user_email: String, new_login: String, callback: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.updateUserLogin(user_email, new_login)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("status")) {
                        callback(jsonObj.get("status").toString())
                    } else {
                        callback("Something went wrong")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    handler.post {
                        callback("Error: $errorMessage")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    callback("Failure: ${t.message}")
                }
            }
        })
}

fun UpdateUserEmail(user_email: String, new_email: String, callback: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.updateUserEmail(user_email, new_email)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("status")) {
                        callback(jsonObj.get("status").toString())
                    } else {
                        callback("Something went wrong")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    handler.post {
                        callback("Error: $errorMessage")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    callback("Failure: ${t.message}")
                }
            }
        })
}

fun UpdateUserPassword(user_email: String, new_password: String, callback: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.updateUserPassword(user_email, new_password)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("status")) {
                        callback(jsonObj.get("status").toString())
                    } else {
                        callback("Something went wrong")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    handler.post {
                        callback("Error: $errorMessage")
                    }
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    callback("Failure: ${t.message}")
                }
            }
        })
}

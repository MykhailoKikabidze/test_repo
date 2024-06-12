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

fun GetProfilePhoto(userEmail: String, callback: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.getProfilePhoto(userEmail)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val photoUrl = response.body() ?: ""
                    handler.post {
                        callback(photoUrl)
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    handler.post {
                        callback("Error: $errorMessage")
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                handler.post {
                    callback("Failure: ${t.message}")
                }
            }
        })
}


fun UpdateProfileImage(userEmail: String, newImageData: String, callback: (String) -> Unit) {
    // Assuming RetrofitClient.instance provides access to your Retrofit API service instance
    RetrofitClient.instance.updateProfileImage(userEmail, newImageData).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                // Call was successful, handle the response as needed
                callback("Profile image updated successfully.")
            } else {
                // Handle cases where the server responds with an error status.
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                callback("Failed to update profile image: $errorMessage")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            // Handle cases where the call to the server failed, e.g., no internet connection, server down, etc.
            callback("Error updating profile image: ${t.message}")
        }
    })
}

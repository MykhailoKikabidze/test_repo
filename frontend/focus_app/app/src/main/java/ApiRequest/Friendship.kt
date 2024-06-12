package ApiRequest

import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun GetFriends(user_email: String, friendsList: (List<String>) -> Unit) {
    RetrofitClient.instance.getFriends(user_email)
        .enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    friendsList(list)
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle failure
            }
        })
}

fun AddFriend(user_email: String, friend_email: String, callback: (String) -> Unit) {
    RetrofitClient.instance.addFriend(user_email, friend_email).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                callback("Success")
            } else {
                callback("Failed")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure ${t.message}")
        }
    })
}

fun DeleteFriend(user_email: String, friend_email: String, callback: (String) -> Unit) {
    RetrofitClient.instance.deleteFriend(user_email, friend_email).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObject = gson.toJson(response.body())
                callback("Success")
            } else {
                callback("Failed")
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure ${t.message}")
        }
    })
}

fun GetUsers(callback: (String) -> Unit) {
    RetrofitClient.instance.getUsers().enqueue(object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val usersJson = gson.toJson(response.body())
                callback(usersJson)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                callback("Error: $errorMessage")
            }
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            callback("Failure: ${t.message}")
        }
    })
}


fun DeleteUser(userEmail: String, callback: (String) -> Unit) {
    RetrofitClient.instance.deleteUser(userEmail).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                callback("User successfully deleted")
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

fun GetUserProfile(userEmail: String, callback: (String) -> Unit) {
    RetrofitClient.instance.getUserProfile(userEmail).enqueue(object : Callback<Any> {
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if (response.isSuccessful) {
                val gson = Gson()
                val jsonObj = gson.toJson(response.body())
                callback(jsonObj)
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
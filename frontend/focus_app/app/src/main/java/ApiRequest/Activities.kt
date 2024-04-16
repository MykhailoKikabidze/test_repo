package ApiRequest

import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun GetCategory(listView: ListView, callback: (String) -> Unit) {
    RetrofitClient.instance.getCategories().enqueue(object : Callback<List<Category>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
            if (response.isSuccessful) {
                val listCategories = response.body() ?: emptyList()
                val categoryNames = listCategories.map { category -> category.name }

                val adapter = ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, categoryNames)

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

fun GetActivities(listView: ListView,cat_name: String, user_email: String, callback: (String) -> Unit) {
    RetrofitClient.instance.getActivities(cat_name,user_email).enqueue(object : Callback<List<Activity>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
            if (response.isSuccessful) {
                val listCategories = response.body() ?: emptyList()
                val activityNames = listCategories.map { activity -> activity.name }

                val adapter = ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, activityNames)

                listView.adapter = adapter

                callback("Categories loaded successfully")
            } else {
                callback("Unknown error")
            }
        }

        override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
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

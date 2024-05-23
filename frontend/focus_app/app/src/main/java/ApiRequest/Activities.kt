package ApiRequest

import ActivitiesAdapter
import Data.activityName
import Data.activityNamesStatic
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun CreateActivityLog(requestBody: Map<String, Any>, cat_name: String, user_email: String, activity_name: String,callback: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.createActivityLog(cat_name, user_email, activity_name, requestBody)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    handler.post {
                        callback("Activity log creation successful")
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


fun GetActivities(recyclerView: RecyclerView, cat_name: String, user_email: String, callback: (String) -> Unit) {
    RetrofitClient.instance.getActivities(cat_name, user_email).enqueue(object : Callback<List<Activity>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
            if (response.isSuccessful) {
                val listActivities = response.body() ?: emptyList()
                val activityNames = listActivities.map { it.name }
                val adapter = recyclerView.adapter as ActivitiesAdapter
                adapter.updateActivities(activityNames)
                callback("Activities loaded successfully")
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

fun GetActivitiesForCharts(cat_name: String, user_email: String, callback: (MutableList<String>) -> Unit) {
    RetrofitClient.instance.getActivities(cat_name, user_email).enqueue(object : Callback<List<Activity>> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<List<Activity>>, response: Response<List<Activity>>) {
            if (response.isSuccessful) {
                val listActivities = response.body() ?: emptyList()
                val activityNames = listActivities.map { it.name }
                var list:MutableList<String> = mutableListOf()
                for(i in activityNames.indices)
                {
                    list+=activityNames[i]
                }
                callback(list)
            } else {
               // callback("Unknown error")
            }
        }

        override fun onFailure(call: Call<List<Activity>>, t: Throwable) {
            handler.post {
               // callback("Failure: ${t.message}")
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

fun UpdateActivity(requestBody: Map<String, String>, cat_name: String, user_email: String, new_activity_name: String, callback: (String) -> Unit) {
    // Удостоверяемся, что requestBody содержит необходимые ключи
    val updatedRequestBody = requestBody.toMutableMap()
    updatedRequestBody["name"] = new_activity_name // Добавляем или обновляем ключ name

    RetrofitClient.instance.updateActivity(updatedRequestBody, cat_name, user_email, new_activity_name).enqueue(object : Callback<Any> {
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


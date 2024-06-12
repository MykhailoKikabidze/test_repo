package ApiRequest

import android.os.Handler
import android.os.Looper
import retrofit2.Call
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Callback
import retrofit2.Response


fun UpdateDailyPoints(user_email:String, date:String, status:(String)->Unit)
{
    RetrofitClient.instance.updateDailyPoints(user_email,date)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if(jsonObj.has("status"))
                    {
                        status(jsonObj.get("status").toString())
                    }
                    else
                    {
                        status("Something goes wrong")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        status("Error: $errorMessage")

                }
            }
            override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                    status("Failure: ${t.message}")
            }
    })
}
fun UpdatePoints(user_email: String,points:Int,action:String,callback: (String)->Unit)
{
    val handler=Handler(Looper.getMainLooper())
    RetrofitClient.instance.updatePoints(user_email,points,action)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if(jsonObj.has("status"))
                    {
                        callback(jsonObj.get("status").toString())
                    }
                    else
                    {
                        callback("Something goes wrong")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    handler.post {
                        callback("Error: $errorMessage")
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                handler.post {
                    callback("Failure: ${t.message}")
                }
            }
        })
}


fun GetPoints(user_email:String,callback: (String)->Unit,points:(Any)->Unit)
{
    val handler = Handler(Looper.getMainLooper())
    RetrofitClient.instance.getPoints(user_email)
        .enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {

//                if(jsonObj.has("status"))
//                {
//                    callback(jsonObj.get("status").toString())
                        points(response.body()!!)

//                }
//                else
//                {
//                    callback("Something goes wrong")
//                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                handler.post {
                    callback("Error: $errorMessage")
                }
            }
        }

        override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
            handler.post {
                callback("Failure: ${t.message}")
            }
        }
    })
}
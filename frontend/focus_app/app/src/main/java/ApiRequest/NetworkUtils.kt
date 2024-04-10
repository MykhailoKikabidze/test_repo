package ApiRequest

import android.content.Context
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Looper
import android.widget.Toast

val connectionProblem="You don't have internet"
fun HandleError(handler:Handler, response: Response<ResponseMessage>, callback: (String) -> Unit)
{
    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
    handler.post {
        callback("Error: $errorMessage")
    }
}

fun CreateUser(requestBody: Map<String,String>, callback: (String) -> Unit)
{
    RetrofitClient.instance.createUser(requestBody).enqueue(object : Callback<Any> {
        private val handler = Handler(Looper.getMainLooper())
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            handleResponse(response,handler,callback)
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            handler.post {
                callback ("Failure: ${t.message}")
            }
        }
    })
}
fun handleResponse(response: Response<Any>,handler: Handler,callback: (String) -> Unit) {
    if (response.isSuccessful) {
        val gson = Gson()
        val jsonObject = gson.toJson(response.body())
        val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

        if (jsonObj.has("login")) {
            callback ("Congratulations")
        } else if (jsonObj.has("num")) {
            callback ("You already have account")
        } else {
            callback ("Unknown error")
        }
    } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        handler.post {
            callback("Error: $errorMessage")
        }
    }
}

fun ConnectServer(callback: (String) -> Unit)
{
    RetrofitClient.instance.getMessage().enqueue(object : Callback<ResponseMessage> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
            if (response.isSuccessful) {
                val message = response.body()?.message ?: connectionProblem
            } else {
                HandleError(handler,response ,callback)
            }
        }

        override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
            handler.post {
                callback("Failure: ${t.message}")
            }
        }
    })
}
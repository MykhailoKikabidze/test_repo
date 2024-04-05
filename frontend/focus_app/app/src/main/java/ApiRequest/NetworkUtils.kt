package ApiRequest

import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Looper

val connectionProblem="You don't have internet"
fun HandleError(handler:Handler, response: Response<ResponseMessage>, messageTextView: TextView)
{
    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
    handler.post {
        messageTextView.text = "Error: $errorMessage"
    }
}

fun CreateUser(requestBody: Map<String,String>, messageTextView: TextView)
{
    RetrofitClient.instance.createUser(requestBody).enqueue(object : Callback<Any> {
        private val handler = Handler(Looper.getMainLooper())
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            handleResponse(response,messageTextView,handler)
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            handler.post {
                messageTextView.text = "Failure: ${t.message}"
            }
        }
    })
}
fun handleResponse(response: Response<Any>, messageTextView: TextView,handler: Handler) {
    if (response.isSuccessful) {
        val gson = Gson()
        val jsonObject = gson.toJson(response.body())
        val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

        if (jsonObj.has("login")) {
            messageTextView.text = "USER"
        } else if (jsonObj.has("num")) {
            messageTextView.text = "ERROR"
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

fun ConnectServer(messageTextView: TextView)
{
    RetrofitClient.instance.getMessage().enqueue(object : Callback<ResponseMessage> {
        private val handler = Handler(Looper.getMainLooper())

        override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
            if (response.isSuccessful) {
                val message = response.body()?.message ?: connectionProblem
            } else {
                HandleError(handler,response ,messageTextView)
            }
        }

        override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
            handler.post {
                messageTextView.text = "Failure: ${t.message}"
            }
        }
    })
}
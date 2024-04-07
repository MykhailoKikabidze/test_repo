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
fun HandleError(handler:Handler, response: Response<ResponseMessage>, messageTextView: TextView)
{
    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
    handler.post {
        messageTextView.text = "Error: $errorMessage"
    }
}

fun CreateUser(requestBody: Map<String,String>,context: Context)
{
    RetrofitClient.instance.createUser(requestBody).enqueue(object : Callback<Any> {
        private val handler = Handler(Looper.getMainLooper())
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            handleResponse(response,handler,context)
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            handler.post {
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        }
    })
}
fun handleResponse(response: Response<Any>,handler: Handler,context: Context) {
    if (response.isSuccessful) {
        val gson = Gson()
        val jsonObject = gson.toJson(response.body())
        val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)

        if (jsonObj.has("login")) {
            Toast.makeText(context, "Congratulations", Toast.LENGTH_SHORT).show()
        } else if (jsonObj.has("num")) {
            Toast.makeText(context, "You already have account", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show()
        }
    } else {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
        handler.post {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
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
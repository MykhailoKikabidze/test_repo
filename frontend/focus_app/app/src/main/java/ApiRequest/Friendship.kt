package ApiRequest

import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun GetFriends(user_email: String,friendsList:(List<String>)->Unit)
{
    RetrofitClient.instance.getFriends(user_email)
        .enqueue(object: Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>,response: Response<List<String>>)
            {
                if(response.isSuccessful)
                {
                    val list= response.body()?: emptyList<String>()
                    val users = list.map {it}
                    friendsList(users)
                }
            }
            override fun onFailure(call: retrofit2.Call<List<String>>, t: Throwable) {

            }
        })
}
fun AddFriend(user_email: String,friend_email:String,callback:(String)->Unit){
    RetrofitClient.instance.addFriend(user_email,friend_email).enqueue( object :Callback<Any>{
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if(response.isSuccessful) {
                val gson=Gson()
                val JsonObject = gson.toJson(response.body())
                val JsonObj = gson.fromJson(JsonObject,JsonObject::class.java)
                callback(JsonObj)
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure ${t.message}")
        }
    })
}

fun DeleteFriend(user_email: String,friend_email:String,callback:(String)->Unit){
    RetrofitClient.instance.deleteFriend(user_email,friend_email).enqueue( object :Callback<Any>{
        override fun onResponse(call: Call<Any>, response: Response<Any>) {
            if(response.isSuccessful) {
                val gson=Gson()
                val JsonObject = gson.toJson(response.body())
                val JsonObj = gson.fromJson(JsonObject,JsonObject::class.java)
                callback(JsonObj)
            }
        }

        override fun onFailure(call: Call<Any>, t: Throwable) {
            callback("Failure ${t.message}")
        }
    })
}

package ApiRequest

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun GetStatisticsActivityDaily(user_email:String, cat_name:String, activity_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsActivityDaily(user_email, cat_name, activity_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })

}

fun GetStatisticsActivityWeekly(user_email:String, cat_name:String, activity_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsActivityWeakly(user_email, cat_name, activity_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })

}

fun GetStatisticsActivityMonthly(user_email:String, cat_name:String, activity_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsActivityMonthly(user_email, cat_name, activity_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })

}

fun GetStatisticsActivityYearly(user_email:String, cat_name:String, activity_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsActivityYearly(user_email, cat_name, activity_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })

}

fun GetStatisticsAcrivityTotaly(user_email:String, cat_name:String, activity_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsActivityTotal(user_email, cat_name, activity_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })
}

fun GetStatisticsCategoryDaily(user_email:String, cat_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsCategoryDaily(user_email, cat_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })
}

fun GetStatisticsCategoryWeekly(user_email:String, cat_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsCategoryWeakly(user_email, cat_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })
}

fun GetStatisticsCategoryYearly(user_email:String, cat_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsCategoryYearly(user_email, cat_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })
}


fun GetStatisticsCategoryMonthly(user_email:String, cat_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsCategoryMonthly(user_email, cat_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                    timeSpend(1)
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                    timeSpend(1)

                }
            }
        })
}

fun GetStatisticsCategoryTotaly(user_email:String, cat_name:String, timeSpend:(Int)->Unit) {
    RetrofitClient.instance.getStatisticsCategoryTotaly(user_email, cat_name)
        .enqueue(object : Callback<Any> {
            private val handler = Handler(Looper.getMainLooper())

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonObject = gson.toJson(response.body())
                    val jsonObj = gson.fromJson(jsonObject, JsonObject::class.java)
                    if (jsonObj.has("time")) {
                        val totalTime=timeStringToSeconds(jsonObj.get("time").toString())
                        timeSpend(totalTime)
                    }


                } else {
                    //callsomething("Unknown error")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                handler.post {
                    // callsomething("Failure: ${t.message}")
                }
            }
        })
}



fun timeStringToSeconds(timeString: String): Int {
    val timeString = timeString.trim('\"')
    val parts = timeString.split(":")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()

    return hours * 3600 + minutes * 60 + seconds

}
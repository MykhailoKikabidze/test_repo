package ApiRequest

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.Locale


interface ApiService {
    @Headers("ngrok-skip-browser-warning: anyValue")
    @GET("/")
    fun getMessage(): Call<ResponseMessage>

    @POST("/users/")
    fun createUser(@Body requestBody: Map<String, String>): Call<Any>

    @POST("/users/email/")
    fun authorization(@Body requestBody: Map<String, String>): Call<Any>

    @GET("/categories/")
    fun getCategories(): Call<List<Category>>

    @POST("/aktivities/name/")
    fun createActivity(@Body requestBody: Map<String, String>): Call<Any>

}
package ApiRequest

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
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

    @POST("/activities/name/")
    fun createActivity(
        @Body requestBody: Map<String, String>,
        @Query("cat_name") cat_name:String,
        @Query("user_email") user_email:String,
    ): Call<Any>

    @HTTP(method = "DELETE", path = "/activities/name/", hasBody = true)
    fun deleteActivity(
        @Body requestBody: Map<String, String>,
        @Query("cat_name") cat_name:String,
        @Query("user_email") user_email:String,
    ): Call<Any>

    @PUT("/activities/name/")
    fun updateActivity(
        @Body requestBody: Map<String, String>,
        @Query("cat_name") catName: String,
        @Query("user_email") userEmail: String,
        @Query("new_activity_name") newActivityName: String
    ): Call<Any>

}
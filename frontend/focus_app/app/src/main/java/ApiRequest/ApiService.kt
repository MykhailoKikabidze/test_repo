package ApiRequest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


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

    @GET("/profile/points/")
    fun getPoints(
        @Query("user_email") user_email:String
    ):Call<Any>

    @GET("/activities/")
    fun getActivities(
        @Query("cat_name") cat_name:String,
        @Query("user_email") user_email:String
    ): Call<List<Activity>>

    @POST("/activity_log/")
    fun createActivityLog(
        @Query("cat_name") cat_name: String,
        @Query("user_email") user_email: String,
        @Query("activity_name") activity_name: String,
        @Body requestBody: Any
    ):Call<Any>

    @POST("/activities/name/")
    fun createActivity(
        @Body requestBody: Map<String, String>,
        @Query("cat_name") cat_name:String,
        @Query("user_email") user_email:String,
    ): Call<Any>

    @DELETE("/activities/name/")
    fun deleteActivity(
        @Query("activity_name") activity_name: String,
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

    @PUT("/profile/points/")
    fun updatePoints(
        @Query("user_email") user_email:String,
        @Query("points") points:Int,
        @Query("action")action:String
    ):Call<Any>


    @GET("/statistics/activity/day/")
    fun getStatisticsActivityDaily(
        @Query("user_email") userEmail: String,
        @Query("cat_name")
        catName: String,
        @Query("activity_name") newActivityName: String
    ): Call<Any>

    @GET("/statistics/activity/weak/")
    fun getStatisticsActivityWeakly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String,
        @Query("activity_name") newActivityName: String
    ): Call<Any>

    @GET("/statistics/activity/month/")
    fun getStatisticsActivityMonthly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String,
        @Query("activity_name") newActivityName: String
    ): Call<Any>

    @GET("/statistics/activity/year/")
    fun getStatisticsActivityYearly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String,
        @Query("activity_name") newActivityName: String
    ): Call<Any>

    @GET("/statistics/activity/all_time/")
    fun getStatisticsActivityTotal(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String,
        @Query("activity_name") newActivityName: String
    ): Call<Any>

    @GET("/statistics/category/day/")
    fun getStatisticsCategoryDaily(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String
    ): Call<Any>

    @GET("/statistics/category/weak/")
    fun getStatisticsCategoryWeakly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String
    ): Call<Any>

    @GET("/statistics/category/month/")
    fun getStatisticsCategoryMonthly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String
    ): Call<Any>

    @GET("/statistics/category/year/")
    fun getStatisticsCategoryYearly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String
    ): Call<Any>

    @GET("/statistics/category/all_time/")
    fun getStatisticsCategoryTotaly(
        @Query("user_email") userEmail: String,
        @Query("cat_name") catName: String
    ): Call<Any>

    @GET("/statistics/total/day/")
    fun getStatisticsTotalDaily(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/statistics/total/weak/")
    fun getStatisticsTotalWeekly(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/statistics/total/month/")
    fun getStatisticsTotalMonthly(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/statistics/total/year/")
    fun getStatisticsTotalYearly(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/statistics/total/all_time/")
    fun getStatisticsTotalTotaly(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/profile/image/")
    fun getProfilePhoto(
        @Query("user_email")userEmail: String
    ):Call<String>

    @PUT("/profile/image/")
    fun updateProfileImage(
        @Query("user_email") userEmail: String,
        @Query("new_image") newImageData: String
    ): Call<Any>

    @PUT("/profile/login/")
    fun updateUserLogin(
        @Query("user_email") userEmail: String,
        @Query("new_login") newLogin: String
    ): Call<Any>



    @PUT("/profile/email/")
    fun updateUserEmail(
        @Query("user_email") userEmail: String,
        @Query("new_email") newEmail: String
    ): Call<Any>

    @PUT("/profile/password/")
    fun updateUserPassword(
        @Query("user_email") userEmail: String,
        @Query("new_password") newPassword: String
    ): Call<Any>

    @POST("/friendship/")
    fun addFriend(
        @Query("user_email")userEmail: String,
        @Query("friend_email")friendEmail: String
    ):Call<Any>

    @DELETE("/friendship/")
    fun deleteFriend(
        @Query("user_email")userEmail: String,
        @Query("friend_email")friendEmail: String
    ):Call<Any>

    @GET("/friendship/")
    fun getFriends(
        @Query("user_email")userEmail: String
    ):Call<List<String>>

    @PUT("/profile/last_log/")
    fun updateDailyPoints(
        @Query("user_email")userEmail: String,
        @Query("new_date_log")date:String
    ):Call<Any>

    @GET("/users/")
    fun getUsers(): Call<List<String>>

    @DELETE("/users/")
    fun deleteUser(
        @Query("user_email") userEmail: String
    ): Call<Any>

    @GET("/users/profile/")
    fun getUserProfile(
        @Query("user_email") userEmail: String
    ): Call<Any>



}

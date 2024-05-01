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

    }
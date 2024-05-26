package ApiRequest

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://73a7-94-254-130-117.ngrok-free.app"

    val instance: ApiService by lazy {
        val moshi = Moshi.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        retrofit.create(ApiService::class.java)
    }
}
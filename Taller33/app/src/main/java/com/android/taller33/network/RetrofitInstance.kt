package com.android.taller33.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto Singleton para proporcionar una única instancia de Retrofit en toda la aplicación.
 * Se ha agregado un Interceptor para loguear la URL final y verificar los parámetros.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // Cliente OkHttp con interceptor para ver la URL en el Logcat
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        val url = request.url.toString()
        Log.d("Retrofit_URL", "Petición enviada a: $url")
        chain.proceed(request)
    }.build()

    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Asignamos el cliente con el log
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}

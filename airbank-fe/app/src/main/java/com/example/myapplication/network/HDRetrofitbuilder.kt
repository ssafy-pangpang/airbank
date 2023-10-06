package com.example.myapplication.network

import com.example.myapplication.AirbankApplication
import com.example.myapplication.api.HDApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HDRetrofitBuilder {
    private const val BASE_URL = "https://airbank.ssafy.life/"
//    private const val BASE_URL = "http://192.168.35.202:8080/"

    @Provides
    @Singleton
    fun HDapiService(): HDApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(AppInterceptor()))
            .build()
            .create(HDApiService::class.java)
    }
    @Provides
    @Singleton
    internal fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor) // okHttp에 인터셉터 추가
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }


    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val JSESSIONID = AirbankApplication.prefs.getString("JSESSIONID","")
            val newRequest = request().newBuilder()
                .addHeader("Cookie", "JSESSIONID=$JSESSIONID")
                .build()
            proceed(newRequest)
        }
    }
}



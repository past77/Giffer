package com.testtask.giphy.giffer.di

import com.testtask.giphy.giffer.data.api.GiphyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [RoomModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(GiphyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi =
        retrofit.create(GiphyApi::class.java)
}
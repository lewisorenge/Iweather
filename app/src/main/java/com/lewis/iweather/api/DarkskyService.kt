package com.lewis.iweather.api

import com.lewis.iweather.model.DarkskyModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.darksky.net/"

interface DarkskyService {

  companion object {
    fun create(): DarkskyService {
      val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
      return retrofit.create(DarkskyService::class.java)
    }
  }

  @GET("forecast/{key}/{latitude},{longitude}")
  fun forecast(
    @Path("key") key: String,
    @Path("latitude") latitude: Double,
    @Path("longitude") longitude: Double,
    @Query("units") units: String,
    @Query("exclude") exclude: String
  ): Call<DarkskyModel.Darksky>

}
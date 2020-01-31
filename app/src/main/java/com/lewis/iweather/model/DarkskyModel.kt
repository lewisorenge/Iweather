package com.lewis.iweather.model

object DarkskyModel {

  data class Darksky(
          val latitude: Double,
          val longitude: Double,
          val timezone: String,
          val currently: Data,
          val daily: Daily)

  data class Daily(
    val summary: String?,
    val icon: String?,
    val data: MutableList<Data>
  )

  data class Data(
    val time: Long,
    val summary: String? = "",
    val icon: String? = "",
    val nearestStormDistance: Int?,
    val nearestStormBearing: Int?,
    val precipIntensity: Double?,
    val precipProbability: Double?,
    val temperature: Double?,
    val apparentTemperature: Double?,
    val apparentTemperatureHigh: Double?,
    val apparentTemperatureLow: Double?,
    val apparentTemperatureHighTime: Long?,
    val apparentTemperatureLowTime: Long?,
    val dewPoint: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
    val windGust: Double?,
    val windGustTime: Long?,
    val windBearing: Int?,
    val cloudCover: Double?,
    val uvIndex: Int?,
    val uvIndexTime: Long?,
    val visibility: Double?,
    val ozone: Double?
  )
}
package com.lewis.iweather.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lewis.iweather.R
import com.lewis.iweather.databinding.ItemDaycardBinding
import com.lewis.iweather.model.DarkskyModel
import com.lewis.iweather.utils.WeatherIcons
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastAdapter
  : RecyclerView.Adapter<DailyForecastAdapter.ForecastViewHolder>() {

  private var dayForecast: MutableList<DarkskyModel.Data> = mutableListOf()
  private lateinit var weatherIcons: Map<String, Drawable>

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding: ItemDaycardBinding
      = DataBindingUtil.inflate(layoutInflater, R.layout.item_daycard, parent, false)

    // Assign weather icons using context
    weatherIcons = WeatherIcons.map(parent.context)
    return ForecastViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return dayForecast.size
  }

  override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

    val day = if (position == 0) {
      "Today"
    } else {
      // Convert UNIX seconds to milliseconds
      val date = Date(dayForecast[position].time * 1000)
      val dateFormatter = SimpleDateFormat("EEEE", Locale.US)
      dateFormatter.format(date)
    }
    holder.binding.dayOfWeek = day
    holder.binding.dailyForecast = dayForecast[position]
    holder.binding.dailyWeatherIcon = weatherIcons[dayForecast[position].icon]

    // Execute binding immediately inside view
    holder.binding.executePendingBindings()
  }

  fun setDayForecast(dayForecast: MutableList<DarkskyModel.Data>?) {
    if (dayForecast == null) {
      Timber.e("dayForecast list passed in is null.")
      return
    }
    this.dayForecast = dayForecast
    notifyDataSetChanged()
  }

  class ForecastViewHolder(val binding: ItemDaycardBinding) : RecyclerView.ViewHolder(binding.root)
}
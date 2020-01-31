package com.lewis.iweather.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lewis.iweather.BuildConfig
import com.lewis.iweather.R
import com.lewis.iweather.databinding.ActivityMainBinding
import com.lewis.iweather.utils.WeatherIcons
import com.lewis.iweather.viewmodel.MainActivityViewModel
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

const val REQUEST_COARSE_LOCATION = 5678

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var viewModel: MainActivityViewModel

  private var weatherIconMap: Map<String, Drawable>? = null

  // Places autocomplete fragment
  private var placeAutoCompleteFragment: PlaceAutocompleteFragment? = null

  // 5 day forecast adapter
  private val adapter = DailyForecastAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    addObservers()

    // Setup toolbar and hide title
    setSupportActionBar(toolbar_mainActivity)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    weatherIconMap = WeatherIcons.map(this)

    viewModel.getUsersCurrentLocation()

    recyclerView_mainActivity_dailyForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    recyclerView_mainActivity_dailyForecast.adapter = adapter
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_toolbar, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.action_search) {
      displayPlacesFragment()
    } else if (item?.itemId == R.id.action_user_location) {
      viewModel.getUsersCurrentLocation()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    if (placeAutoCompleteFragment != null && placeAutoCompleteFragment!!.isVisible) {
      hidePlacesFragment()
      return
    }
    super.onBackPressed()
  }

  private fun displayPlacesFragment() {
    if (placeAutoCompleteFragment == null) {

      val options = PlaceOptions.builder()
        .toolbarColor(ContextCompat.getColor(this, R.color.materialGray_50))
        .backgroundColor(ContextCompat.getColor(this, R.color.materialGray_50))
        .geocodingTypes(GeocodingCriteria.TYPE_PLACE, GeocodingCriteria.TYPE_REGION)
        .build()

      window.statusBarColor = ContextCompat.getColor(this, R.color.materialGray_500)
      placeAutoCompleteFragment = PlaceAutocompleteFragment.newInstance(BuildConfig.MAPBOX_ACCESS_TOKEN, options)
      placeAutoCompleteFragment!!.setOnPlaceSelectedListener(viewModel)

      val transaction = supportFragmentManager.beginTransaction()
      transaction.add(R.id.fragment_container, placeAutoCompleteFragment!!, PlaceAutocompleteFragment.TAG)
      transaction.commit()
    } else {

      showPlacesFragment()
    }
  }

  private fun showPlacesFragment() {
    window.statusBarColor = ContextCompat.getColor(this, R.color.materialGray_500)
    supportFragmentManager.beginTransaction().show(placeAutoCompleteFragment!!).commit()
  }

  private fun hidePlacesFragment() {
    window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    val inputMethod = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethod.hideSoftInputFromWindow(textView_mainActivity_wind.windowToken, 0)
    supportFragmentManager.beginTransaction().hide(placeAutoCompleteFragment!!).commit()
  }

  private fun addObservers() {
    viewModel.requestLocationPermissionLiveData.observe(this, Observer { shouldRequestPermission ->
      if (shouldRequestPermission) {
        Timber.v("requesting permission")
        requestPermissions()
      }
    })

    viewModel.locationNameLiveData.observe(this, Observer { locationName ->
      binding.locationName = locationName
    })

    viewModel.darkSkyApiResponseLiveData.observe(this, Observer { darkSkyModel ->
      binding.currentCondition = darkSkyModel.currently

      adapter.setDayForecast(darkSkyModel.daily.data)

      // Bind the current weather icon
      if (darkSkyModel.currently.icon != null && weatherIconMap != null) {
        binding.currentConditionIcon = weatherIconMap!![darkSkyModel.currently.icon]
      }
    })

    viewModel.userFinishedSearchLiveData.observe(this, Observer { canceled ->
      Timber.v("User has canceled geocoding search.")
      if (canceled && placeAutoCompleteFragment != null) {
        hidePlacesFragment()
      }
    })
  }

  private fun requestPermissions() {
    ActivityCompat.requestPermissions(this,
      arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == REQUEST_COARSE_LOCATION && grantResults.isNotEmpty()
      && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Timber.v("User gave location permission, continue with getting user's last location.")
      viewModel.getUsersCurrentLocation()
    } else {
      Timber.v("User refused to give location permission. Continue using the default location.")
    }
  }
}

# Iweather
Iweather is  weather app using Dark Sky's forecast API. When the app is initially opened, the device's current location is acquired and a request to the Dark Sky API is made to fetch the current and future weather conditions. While Dark Sky provides an abundance of data such as the weather summary, precipitation odds, and humidity, only some of this info is displayed within the UI. Inside the data model class however, we are obtaining all of this information making it easy to add additional information to the UI in the future. The app has two main views: the home screen where the weather information is displayed, and a second screen where the user can select the location they want information for. That second screen is called by tapping the search icon at the top of the screen. At any point the user can opt to see the weather for their current location by tapping the icon to the right of the search icon.

[_Powered by Dark Sky_](https://darksky.net/poweredby/)
## Building project

### API Keys
In order to hide the access tokens for Mapbox and Dark Sky, access tokens are purposely not checked into version control. Thus, if you checkout this project and try compiling, the build will fail. To resolve this issue, open your local `gradle.properties` file stored in `<USER NAME>/.gradle/gradle.properties` (on Mac) and add the following lines with your own access token:
mapboxAccessToken="MAPBOX_ACCESS_TOKEN_HERE"
darkskyKey="DARK_SKY_TOKEN_HERE"

## Architecture
It is written in Kotlin using a single activity MVVM architecture. The app relies heavily on the [Android Bindings library](https://developer.android.com/topic/libraries/data-binding/) to populate the views and the [ViewModel/LiveData](https://developer.android.com/topic/libraries/architecture/viewmodel) Android Architecture Components library to extract logical data from the app's views.

The app targets the latest API level, with a min sdk version of 21, and makes use of the new [Material Design Components](https://material.io/develop/android/docs/getting-started/) library (The base app theme extends the material dark style.)

To allow users the ability to query additional location weather conditions besides their current locations, I used The [Mapbox Places Plugin](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-places), using the underlying Mapbox Geocoding APIs. Additionally, I had the app make a reverse geocoding API call when first opening. This is how it populates the place name of the device's current location in a human-readable format rather than displaying raw lat/lng points in the app's toolbar.

All API calls are made using Retrofit and the resulting JSON is deserialized using the popular GSON library.

Some additional libraries used in this project include:
- Timber for logging
- Constraint Layout
- Google Play Location services for gathering the users location
- Android Compatibility library for backward API support

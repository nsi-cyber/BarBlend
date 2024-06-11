package com.nsicyber.barblend.common

object Constants {
    const val BASE_URL = "https://the-cocktail-db.p.rapidapi.com/"
    const val PERIODIC_WORK_NAME = "NotificationWorker"
    const val NOTIFICATION_CHANNEL = "notification_channel"
    const val COCKTAIL_DATABASE_NAME = "cocktail_database"

    object Endpoints {
        const val POPULAR = "/popular.php"
        const val LOOKUP = "/lookup.php"
        const val SEARCH = "/search.php"
        const val RANDOM = "/random.php"
        const val LATEST = "/latest.php"
    }

    object DataStore {
        const val USER_PREFERENCES_NAME = "user_preferences"
        const val USER_PREFERENCES_KEY = "latest_cocktail"
    }

    object OkHttpKeys {
        const val API_KEY_HEADER = "X-RapidAPI-Key"
        const val API_HOST_HEADER = "X-RapidAPI-Host"
        const val API_HOST_VALUE = "the-cocktail-db.p.rapidapi.com"
    }
}

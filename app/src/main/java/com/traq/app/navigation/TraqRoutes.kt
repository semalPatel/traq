package com.traq.app.navigation

sealed class TraqRoute(val route: String) {
    data object Dashboard : TraqRoute("dashboard")
    data object Tracking : TraqRoute("tracking/{tripId}") {
        fun create(tripId: String) = "tracking/$tripId"
    }
    data object History : TraqRoute("history")
    data object TripDetail : TraqRoute("trip/{tripId}") {
        fun create(tripId: String) = "trip/$tripId"
    }
    data object Settings : TraqRoute("settings")
    data object Onboarding : TraqRoute("onboarding")
}

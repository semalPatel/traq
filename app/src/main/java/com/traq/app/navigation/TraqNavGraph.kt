package com.traq.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.LatLng
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.api.RenderMapView
import com.traq.core.maps.api.RoutePolyline
import com.traq.core.ui.theme.TraqTeal
import com.traq.feature.dashboard.ui.DashboardScreen
import com.traq.feature.history.ui.HistoryScreen
import com.traq.feature.settings.ui.SettingsScreen
import com.traq.feature.tracking.ui.TrackingScreen
import com.traq.feature.tripdetail.ui.TripDetailScreen

@Composable
fun TraqNavGraph(
    navController: NavHostController = rememberNavController(),
    mapRenderer: MapRenderer? = null
) {
    NavHost(navController = navController, startDestination = TraqRoute.Dashboard.route) {
        composable(TraqRoute.Dashboard.route) {
            DashboardScreen(
                onStartTrip = { tripId -> navController.navigate(TraqRoute.Tracking.create(tripId)) },
                onTripClick = { tripId -> navController.navigate(TraqRoute.TripDetail.create(tripId)) },
                onHistoryClick = { navController.navigate(TraqRoute.History.route) },
                onSettingsClick = { navController.navigate(TraqRoute.Settings.route) }
            )
        }
        composable(
            TraqRoute.Tracking.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) {
            TrackingScreen(
                onTripCompleted = { tripId ->
                    navController.navigate(TraqRoute.TripDetail.create(tripId)) {
                        popUpTo(TraqRoute.Dashboard.route)
                    }
                },
                onBack = { navController.popBackStack() },
                mapContent = { modifier, cameraPosition, routePoints ->
                    TrackingMapContent(mapRenderer, modifier, cameraPosition, routePoints)
                }
            )
        }
        composable(TraqRoute.History.route) {
            HistoryScreen(
                onTripClick = { tripId -> navController.navigate(TraqRoute.TripDetail.create(tripId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            TraqRoute.TripDetail.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) {
            TripDetailScreen(onBack = { navController.popBackStack() })
        }
        composable(TraqRoute.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
private fun TrackingMapContent(
    mapRenderer: MapRenderer?,
    modifier: Modifier,
    cameraPosition: CameraPosition,
    routePoints: List<LatLng>
) {
    if (mapRenderer != null) {
        RenderMapView(
            renderer = mapRenderer,
            modifier = modifier,
            cameraPosition = cameraPosition,
            polylines = if (routePoints.size >= 2) listOf(
                RoutePolyline(
                    points = routePoints,
                    colorInt = TraqTeal.toArgb(),
                    widthDp = 5f
                )
            ) else emptyList(),
            markers = emptyList(),
            onCameraMove = {}
        )
    }
}

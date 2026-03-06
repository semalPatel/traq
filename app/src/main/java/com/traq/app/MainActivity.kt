package com.traq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.traq.app.navigation.TraqNavGraph
import com.traq.core.maps.api.MapRenderer
import com.traq.core.permissions.PermissionManager
import com.traq.core.ui.theme.TraqTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mapRenderer: MapRenderer

    @Inject
    lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val needsOnboarding = !permissionManager.hasLocationPermission()
        setContent {
            TraqTheme {
                TraqNavGraph(
                    mapRenderer = mapRenderer,
                    startOnboarding = needsOnboarding
                )
            }
        }
    }
}

package com.traq.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.traq.app.navigation.TraqNavGraph
import com.traq.core.maps.api.MapRenderer
import com.traq.core.ui.theme.TraqTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mapRenderer: MapRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraqTheme {
                TraqNavGraph(mapRenderer = mapRenderer)
            }
        }
    }
}

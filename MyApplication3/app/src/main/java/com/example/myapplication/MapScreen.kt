package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    // Coordinates for the University of Oulu
    val studyLocation = LatLng(65.0593, 25.4663)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(studyLocation, 14f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = studyLocation),
            title = "Study Spot",
            snippet = "Practicing Finnish vocabulary here!"
        )
    }
}
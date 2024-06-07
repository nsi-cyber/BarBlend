package com.nsicyber.barblend

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nsicyber.barblend.presentation.navigation.NavigationGraph
import com.nsicyber.barblend.ui.theme.BarBlendTheme
import dagger.hilt.android.AndroidEntryPoint

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.nsicyber.barblend.common.InternetConnectionCallback
import com.nsicyber.barblend.common.InternetConnectionObserver


@AndroidEntryPoint
class MainActivity : ComponentActivity(), InternetConnectionCallback {
    private var isConnected by mutableStateOf<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InternetConnectionObserver
            .instance(this)
            .setCallback(this)
            .register().checkInitialInternetConnection {
                isConnected = it
            }

        setContent {
            isConnected?.let { connected ->
                NavigationGraph(isConnected = connected)
            } ?: run {
                // Display a loading screen or a splash screen
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Add your loading screen or splash screen here
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        InternetConnectionObserver.unRegister()
    }

    override fun onConnected() {
        if (isConnected == false) {
            isConnected = true
            Toast.makeText(this, "Internet connection is established", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onDisconnected() {
        isConnected = false
        Toast.makeText(this, "Internet connection is lost", Toast.LENGTH_LONG).show()
    }

}

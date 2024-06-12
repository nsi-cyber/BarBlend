package com.nsicyber.barblend

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.nsicyber.barblend.common.InternetConnectionCallback
import com.nsicyber.barblend.common.InternetConnectionObserver
import com.nsicyber.barblend.presentation.navigation.NavigationGraph
import com.nsicyber.barblend.ui.theme.BarBlendTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), InternetConnectionCallback {
    private var isConnected by mutableStateOf<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission()
        }

        InternetConnectionObserver
            .instance(this)
            .setCallback(this)
            .register().checkInitialInternetConnection {
                isConnected = it
            }

        setContent {
            BarBlendTheme {
                isConnected?.let { connected ->
                    NavigationGraph(isConnected = connected)
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
            Toast.makeText(
                this,
                this.getText(R.string.toast_message_on_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    override fun onDisconnected() {
        isConnected = false
        Toast.makeText(
            this,
            this.getText(R.string.toast_message_on_disconnected),
            Toast.LENGTH_LONG,
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionRationale()
            }

            else -> {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) {
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale() {
        requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
}

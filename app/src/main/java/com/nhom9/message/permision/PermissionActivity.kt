package com.nhom9.message.permision

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionCheck(permission: String, onPermissionGranted:()->Unit){
    val permissionState= rememberPermissionState(permission = permission)
    if(permissionState.hasPermission){
        onPermissionGranted.invoke()
        return
    }
    else{
        SideEffect {
            permissionState.launchPermissionRequest()
        }
        return
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
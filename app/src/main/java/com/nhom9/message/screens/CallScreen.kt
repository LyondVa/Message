package com.nhom9.message.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nhom9.message.MViewModel
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.activecall.CallContent
import io.getstream.video.android.compose.ui.components.call.controls.ControlActions
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.call.state.CallAction
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.call.state.ToggleSpeakerphone
import kotlinx.coroutines.launch
import android.app.Activity
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.getstream.video.android.compose.ui.components.call.controls.actions.FlipCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.LeaveCallAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleCameraAction
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleMicrophoneAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
@Composable
fun CallScreen(navController: NavController, MViewModel: MViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val call = MViewModel.call
    val members = MViewModel.members
    val onEndCall = {
        scope.launch {
            call.end()
        }
        navController.popBackStack()
    }
    val onBackPressed = {
        navController.popBackStack()
    }
    Log.d("TAG", "Done")

    LaunchCallPermissions(
        call = call,
        onAllPermissionsGranted = {
            // All permissions are granted, so we can join the call.
            scope.launch {
                call.create(memberIds = members)
                //call.ring()
                val result = call.join()
            }
        }
    )
    VideoTheme{
        val onCallAction: (CallAction) -> Unit = { callAction ->
            when (callAction) {
                is ToggleCamera -> call.camera.setEnabled(callAction.isEnabled)
                is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                is ToggleSpeakerphone -> call.speaker.setEnabled(callAction.isEnabled)
                is FlipCamera -> call.camera.flip()
                is LeaveCall -> onEndCall.invoke()
                else -> onBackPressed.invoke()
            }
        }
        CallContent(
            modifier = Modifier.fillMaxSize(),
            call = call,
            onBackPressed = {onBackPressed.invoke()},
            onCallAction = onCallAction
        )
    }
}
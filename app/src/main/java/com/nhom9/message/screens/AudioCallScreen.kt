package com.nhom9.message.screens

import android.annotation.SuppressLint
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
import io.getstream.video.android.compose.ui.components.call.ringing.RingingCallContent
import io.getstream.video.android.core.call.state.CallAction
import io.getstream.video.android.core.call.state.FlipCamera
import io.getstream.video.android.core.call.state.LeaveCall
import io.getstream.video.android.core.call.state.ToggleCamera
import io.getstream.video.android.core.call.state.ToggleMicrophone
import io.getstream.video.android.core.call.state.ToggleSpeakerphone
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation", "CoroutineCreationDuringComposition")
@Composable
fun AudioCallScreen(navController: NavController, MViewModel: MViewModel) {
    val scope = rememberCoroutineScope()
    LocalContext.current
    val call = MViewModel.call
    val members = MViewModel.members

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
                is ToggleCamera -> call.camera.setEnabled(false)
                is ToggleMicrophone -> call.microphone.setEnabled(callAction.isEnabled)
                is ToggleSpeakerphone -> call.speaker.setEnabled(callAction.isEnabled)
                is FlipCamera -> call.camera.flip()
                is LeaveCall -> navController.popBackStack()
                else -> Unit
            }
        }
        RingingCallContent(
            modifier = Modifier.background(color = VideoTheme.colors.brandRed),
            call = call,
//                onBackPressed = { finish() },
            isVideoType = true,
            onAcceptedContent = {
                CallContent(
                    modifier = Modifier.fillMaxSize(),
                    call = call,
                    onBackPressed = {navController.popBackStack()},
                    onCallAction = onCallAction
                )
            },
            onRejectedContent = {
                // do something when a call is rejected
            },
            onCallAction = onCallAction
        )
    }
}
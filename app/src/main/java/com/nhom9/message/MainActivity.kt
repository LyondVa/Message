package com.nhom9.message

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nhom9.message.screens.AudioCallScreen
import com.nhom9.message.screens.CallScreen
import com.nhom9.message.screens.ChatImageScreen
import com.nhom9.message.screens.ChatListScreen
import com.nhom9.message.screens.ChatProfileScreen
import com.nhom9.message.screens.EntryScreen
import com.nhom9.message.screens.LoginScreen
import com.nhom9.message.data.USERID
import com.nhom9.message.screens.ProfileScreen
import com.nhom9.message.screens.chatrequestscreen.ChatRequestScreen
import com.nhom9.message.screens.chatrequestscreen.UserProfileScreen
import com.nhom9.message.screens.chatscreens.ChatImageScreen
import com.nhom9.message.screens.chatscreens.ChatListScreen
import com.nhom9.message.screens.chatscreens.ChatProfileScreen
import com.nhom9.message.screens.chatscreens.SingleChatScreen
import com.nhom9.message.screens.entryscreens.EntryScreen
import com.nhom9.message.screens.entryscreens.LogInWithPhoneNumberScreen
import com.nhom9.message.screens.entryscreens.LoginScreen
import com.nhom9.message.screens.entryscreens.SignUpScreen
import com.nhom9.message.screens.entryscreens.SignUpWithPhoneNumberScreen
import com.nhom9.message.screens.reportscreens.ReportOptionScreen
import com.nhom9.message.screens.reportscreens.ReportScreen
import com.nhom9.message.screens.statusscreens.SingleStatusScreen
import com.nhom9.message.screens.statusscreens.StatusScreen
import com.nhom9.message.screens.subsettingscreens.AccountSettingScreen
import com.nhom9.message.screens.subsettingscreens.DisplaySettingScreen
import com.nhom9.message.screens.subsettingscreens.NotificationAndSoundSettingScreen
import com.nhom9.message.screens.subsettingscreens.PrivacyAndSecuritySettingScreen
import com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens.EditNameScreen
import com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens.EditPhoneNumberScreen
import com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens.EditProfileImageScreen
import com.nhom9.message.screens.subsettingscreens.displaysettingsubscreens.ChangeLanguageScreen
import com.nhom9.message.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route: String) {
    data object Entry : DestinationScreen(route = "entry")
    data object SignUp : DestinationScreen(route = "signup")
    data object Login : DestinationScreen(route = "login")
    data object Profile : DestinationScreen(route = "profile")
    data object ChatList : DestinationScreen(route = "chatList")
    data object SingleChat : DestinationScreen(route = "singleChat/{chatId}") {
        fun createRoute(id: String?) = "singleChat/$id" //custom??x
    }

    data object StatusList : DestinationScreen(route = "statusList")
    data object SingleStatus : DestinationScreen(route = "singleStatus/{userId}") {
        fun createRoute(userId: String) = "singleStatus/$userId"
    }

    data object AccountSetting : DestinationScreen(route = "accountSetting")
    data object DisplaySetting : DestinationScreen(route = "displaySetting")
    data object NotificationAndSoundSetting :
        DestinationScreen(route = "notificationAndSoundSetting")

    data object PrivacyAndSecuritySetting : DestinationScreen(route = "privacyAndSecuritySetting")
    data object EditName : DestinationScreen(route = "editName")
    data object EditPhoneNumber : DestinationScreen(route = "editPhoneNumber")
    data object EditProfileImage : DestinationScreen(route = "editProfileImage")

    data object VideoCall : DestinationScreen(route = "callScreen")
    data object AudioCall : DestinationScreen(route = "callScreen")
    data object ChatProfile : DestinationScreen(route = "chatProfile/{userId}") {
        fun createRoute(userId: String) = "chatProfile/$userId"
    }

    data object ChatImage : DestinationScreen(route = "chatImage/{imageUrl}") {
        fun createRoute(imageUrl: String) = "chatImage/$imageUrl"
    }

    data object ChangeLanguage : DestinationScreen(route = "changeLanguage")
    data object Report : DestinationScreen(route = "report/{userId}") {
        fun createRoute(userId: String) = "report/$userId"
    }

    data object ReportOption :
        DestinationScreen(route = "reportOption/{reportOptionIndex}/{userId}") {
        fun createRoute(reportOptionIndex: String, userId: String) =
            "reportOption/$reportOptionIndex/$userId"
    }

    data object SignUpWithPhoneNumber : DestinationScreen(route = "signUpWithPhoneNumber")
    data object LogInWithPhoneNumber : DestinationScreen(route = "logInWithPhoneNumber")
    data object ChatRequestScreen : DestinationScreen(route = "chatRequestScreen")
    data object UserProfile : DestinationScreen(route = "userProfile/{isMyRequest}/{userId}") {
        fun createRoute(userId: String, isMyRequest: String) = "userProfile/$isMyRequest/$userId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        setContent {
            AppTheme {
                Surface(
                    tonalElevation = 5.dp,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }


    @Composable
    fun ChatAppNavigation() {
        val viewModel = hiltViewModel<MViewModel>()
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = DestinationScreen.Entry.route) {
            composable(DestinationScreen.Entry.route) {
                EntryScreen(navController, viewModel)
            }
            composable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, viewModel)
            }
            composable(DestinationScreen.Login.route) {
                LoginScreen(navController, viewModel)
            }
            composable(DestinationScreen.ChatList.route) {
                ChatListScreen(navController, viewModel)
            }
            composable(DestinationScreen.SingleChat.route) {
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController, viewModel, chatId)
                }
            }
            composable(DestinationScreen.VideoCall.route){
                CallScreen(navController, viewModel)
            }
            composable(DestinationScreen.AudioCall.route){
                AudioCallScreen(navController, viewModel)
            }
            composable(DestinationScreen.StatusList.route) {
                StatusScreen(navController, viewModel)
            }
            composable(DestinationScreen.SingleStatus.route) {
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(navController, viewModel, userId)
                }
            }
            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController, viewModel)
            }
            composable(DestinationScreen.AccountSetting.route) {
                AccountSettingScreen(navController, viewModel)
            }
            composable(DestinationScreen.DisplaySetting.route) {
                DisplaySettingScreen(navController, viewModel)
            }
            composable(DestinationScreen.NotificationAndSoundSetting.route) {
                NotificationAndSoundSettingScreen(navController, viewModel)
            }
            composable(DestinationScreen.PrivacyAndSecuritySetting.route) {
                PrivacyAndSecuritySettingScreen(navController, viewModel)
            }
            composable(DestinationScreen.EditName.route) {
                EditNameScreen(navController, viewModel)
            }
            composable(DestinationScreen.EditPhoneNumber.route) {
                EditPhoneNumberScreen(navController, viewModel)
            }
            composable(DestinationScreen.EditProfileImage.route) {
                EditProfileImageScreen(navController, viewModel)
            }
            composable(DestinationScreen.ChatProfile.route) {
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    ChatProfileScreen(navController, viewModel, userId)
                }
            }
            composable(DestinationScreen.ChatImage.route) {
                val imageUrl = it.arguments?.getString("imageUrl")
                imageUrl?.let {
                    ChatImageScreen(navController, imageUrl)
                }
            }
            composable(DestinationScreen.ChangeLanguage.route) {
                ChangeLanguageScreen(navController, viewModel)
            }
            composable(DestinationScreen.Report.route) {
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    ReportScreen(navController, userId)
                }
            }
            composable(DestinationScreen.ReportOption.route) {
                val reportOptionIndex = it.arguments?.getString("reportOptionIndex")
                val userId = it.arguments?.getString("userId")
                reportOptionIndex?.let {
                    ReportOptionScreen(navController, viewModel, reportOptionIndex, userId!!)
                }
            }
            composable(DestinationScreen.SignUpWithPhoneNumber.route) {
                SignUpWithPhoneNumberScreen(navController, viewModel)
            }
            composable(DestinationScreen.LogInWithPhoneNumber.route) {
                LogInWithPhoneNumberScreen(navController, viewModel)
            }
            composable(DestinationScreen.ChatRequestScreen.route) {
                ChatRequestScreen(navController, viewModel)
            }
            composable(DestinationScreen.UserProfile.route) {
                val userId = it.arguments?.getString(USERID)
                val isMyRequest = it.arguments?.getString("isMyRequest")
                userId?.let {
                    UserProfileScreen(navController, viewModel, userId, isMyRequest!!.toBoolean())
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

}

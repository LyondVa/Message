package com.nhom9.message

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nhom9.message.screens.ChatImageScreen
import com.nhom9.message.screens.ChatListScreen
import com.nhom9.message.screens.ChatProfileScreen
import com.nhom9.message.screens.EntryScreen
import com.nhom9.message.screens.LoginScreen
import com.nhom9.message.screens.ProfileScreen
import com.nhom9.message.screens.SignUpScreen
import com.nhom9.message.screens.SingleChatScreen
import com.nhom9.message.screens.SingleStatusScreen
import com.nhom9.message.screens.StatusScreen
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
    object Entry : DestinationScreen(route = "entry")
    object SignUp : DestinationScreen(route = "signup")
    object Login : DestinationScreen(route = "login")
    object Profile : DestinationScreen(route = "profile")
    object ChatList : DestinationScreen(route = "chatList")
    object SingleChat : DestinationScreen(route = "singleChat/{chatId}") {
        fun createRoute(id: String?) = "singleChat/$id" //custom??x
    }

    object StatusList : DestinationScreen(route = "statusList")
    object SingleStatus : DestinationScreen(route = "singleStatus/{userId}") {
        fun createRoute(userId: String) = "singleStatus/$userId"
    }

    object AccountSetting : DestinationScreen(route = "accountSetting")
    object DisplaySetting : DestinationScreen(route = "displaySetting")
    object NotificationAndSoundSetting : DestinationScreen(route = "notificationAndSoundSetting")
    object PrivacyAndSecuritySetting : DestinationScreen(route = "privacyAndSecuritySetting")
    object EditName : DestinationScreen(route = "editName")
    object EditPhoneNumber : DestinationScreen(route = "editPhoneNumber")
    object EditProfileImage : DestinationScreen(route = "editProfileImage")
    object ChatProfile : DestinationScreen(route = "chatProfile/{userId}") {
        fun createRoute(userId: String) = "chatProfile/$userId"
    }

    object ChatImage : DestinationScreen(route = "chatImage/{imageUrl}") {
        fun createRoute(imageUrl: String) = "chatImage/$imageUrl"
    }

    object ChangeLanguage : DestinationScreen(route = "changeLanguage")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
            ) {
                // A surface container using the 'background' color from the theme
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
    }
}

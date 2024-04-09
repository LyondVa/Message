package com.nhom9.message

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nhom9.message.screens.ChatListScreen
import com.nhom9.message.screens.LoginScreen
import com.nhom9.message.screens.ProfileScreen
import com.nhom9.message.screens.SignUpScreen
import com.nhom9.message.screens.StatusScreen
import com.nhom9.message.ui.theme.MessageTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route : String){
    object SignUp : DestinationScreen(route = "signup")
    object Login : DestinationScreen(route = "login")
    object Profile : DestinationScreen(route = "profile")
    object ChatList : DestinationScreen(route = "chatList")
    object SingleChat: DestinationScreen(route = "singleChat/{chatId}"){
        fun createRoute(id : String?) = "singleChat/$id" //custom??
    }

    object StatusList : DestinationScreen(route = "statusList")
    object SingleStatus: DestinationScreen(route = "singleStatus/{statusId}"){
        fun createRoute(id : String) = "singleStatus/$id"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
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
fun ChatAppNavigation(){
    val viewModel = hiltViewModel<MViewModel>()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController, viewModel)
        }
        composable(DestinationScreen.Login.route){
            LoginScreen(navController, viewModel)
        }
        composable(DestinationScreen.ChatList.route){
            ChatListScreen(navController, viewModel)
        }
        composable(DestinationScreen.StatusList.route){
            StatusScreen(navController, viewModel)
        }
        composable(DestinationScreen.Profile.route){
            ProfileScreen(navController, viewModel)
        }
    }
}

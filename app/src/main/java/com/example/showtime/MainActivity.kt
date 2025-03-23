package com.example.showtime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.showtime.ui.theme.ShowTimeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShowTimeTheme {
                SplashScreen()
            }
        }
    }
}


//  Navigation Setup
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
    }
}

// ✅ Splash Screen with Navigation
@Composable
fun SplashScreen(navController: NavHostController? = null) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(4000) // 4-second splash delay
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as? Activity)?.finish() // Close Splash Screen after transition
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo over the background
        Image(
            painter = painterResource(id = R.drawable.background8),
            contentDescription = "ShowTime Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(400.dp)
                .clip(RoundedCornerShape(50.dp))
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                    shape = RoundedCornerShape(50.dp)
                    clip = true
                }
                .padding(16.dp)
        )
    }
}



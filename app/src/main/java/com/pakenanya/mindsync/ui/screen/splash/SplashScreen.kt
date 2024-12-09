package com.pakenanya.mindsync.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pakenanya.mindsync.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: (String?) -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()
    val token by viewModel.token.collectAsState()

    var isInitialCheckComplete by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        delay(2000)
        isInitialCheckComplete = true
    }

    LaunchedEffect(isInitialCheckComplete, token) {
        if (isInitialCheckComplete) {
            onSplashComplete(token)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.mindsync_login),
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp)
            )
        }
    }
}
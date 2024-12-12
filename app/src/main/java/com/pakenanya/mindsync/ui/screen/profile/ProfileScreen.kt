package com.pakenanya.mindsync.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pakenanya.mindsync.R
import com.pakenanya.mindsync.ui.navigation.Routes
import com.pakenanya.mindsync.ui.screen.auth.AuthState
import com.pakenanya.mindsync.ui.screen.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val authState = authViewModel.authState.observeAsState()
    val userData = authViewModel.userData.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(Routes.PROFILE)
                        },
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.profile),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                userData.value?.let { Text(it.username, style = MaterialTheme.typography.headlineSmall) }

                userData.value?.let { Text(it.email, style = MaterialTheme.typography.bodyMedium) }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate(Routes.CONNECT_BOT) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF00AD11)
                    ),
                ) {
                    Text("Hubungkan ke Whatsapp")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(Routes.EDIT_PROFILE) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF1A1344)
                    ),
                ) {
                    Text("Edit Profil")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Logout
                Button(
                    onClick = { authViewModel.signOut() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF1A1344)
                    ),
                ) {
                    Text("Logout", color = Color.White)
                }
            }
        }
    }
}
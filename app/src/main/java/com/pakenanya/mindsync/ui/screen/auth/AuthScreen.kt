package com.pakenanya.mindsync.ui.screen.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.pakenanya.mindsync.ui.theme.MindsyncTheme

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit
) {

}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
fun AuthScreenPreview() {
    MindsyncTheme {
        AuthScreen(onSuccess = {})
    }
}
package com.pakenanya.mindsync.ui.screen.article

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.pakenanya.mindsync.ui.theme.MindsyncTheme

@Composable
fun ArticleScreen() {

}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun ArticleScreenPreview() {
    MindsyncTheme {
        ArticleScreen()
    }
}
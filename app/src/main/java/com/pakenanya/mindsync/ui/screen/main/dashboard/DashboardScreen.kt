package com.pakenanya.mindsync.ui.screen.main.dashboard

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pakenanya.mindsync.R
import com.pakenanya.mindsync.ui.theme.MindsyncTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: DashboardViewModel = hiltViewModel()

    val searchQuery = viewModel.searchQuery.observeAsState("")
    val notesData = viewModel.notesData.observeAsState()

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 20.dp, bottom = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TextField(
                value = searchQuery.value,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onSearchSubmitted()
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Results", style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                notesData.value?.let {
                    items(it.size) { index ->
                        DashboardItem(
                            id = it[index].id,
                            title = "Kegiatan Hari $index",
                            description = it[index].text,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardItem(id: Int, title: String, description: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("note_detail/$id") },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
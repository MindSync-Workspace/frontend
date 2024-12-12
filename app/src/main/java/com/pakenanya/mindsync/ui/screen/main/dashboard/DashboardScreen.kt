package com.pakenanya.mindsync.ui.screen.main.dashboard

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pakenanya.mindsync.R
import com.pakenanya.mindsync.ui.screen.main.ContentState
import com.pakenanya.mindsync.ui.screen.main.note.NoteItem
import com.pakenanya.mindsync.ui.theme.MindsyncTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: DashboardViewModel = hiltViewModel()

    val searchQuery = viewModel.searchQuery.observeAsState("")
    val notesData = viewModel.notesData.observeAsState()
    val searchState = viewModel.searchState.observeAsState()

    val focusManager = LocalFocusManager.current

    val textFieldStates = remember { mutableStateMapOf<String, Boolean>() }
    fun getBorderColor(key: String): Color {
        return if (textFieldStates[key] == true) Color(0xFF006FFD) else Color(0xFFC5C6CC)
    }

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
                placeholder = { Text("Cari catatan") },
                colors = TextFieldDefaults.colors(
                    Color.Black,
                    cursorColor = Color(0xFF006FFD),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .onFocusChanged { focusState ->
                        textFieldStates["search"] = focusState.isFocused
                    }
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = getBorderColor("search")
                        ),
                        shape = RoundedCornerShape(12)
                    ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onSearchSubmitted()
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                suffix = {
                    IconButton(
                        onClick = {
                            viewModel.onSearchSubmitted()
                            focusManager.clearFocus()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Hasil pencarian", style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                notesData.value?.let {
                    items(it.size) { index ->
                        NoteItem(
                            note_id = it[index].id,
                            notes = it[index].text,
                            createdAt = it[index].createdAt!!,
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
        if (searchState.value is SearchState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color(0x80000000),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
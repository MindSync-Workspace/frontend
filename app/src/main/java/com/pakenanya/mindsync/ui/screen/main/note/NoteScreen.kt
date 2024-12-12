package com.pakenanya.mindsync.ui.screen.main.note

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pakenanya.mindsync.ui.navigation.Routes
import com.pakenanya.mindsync.ui.screen.main.ContentState
import com.pakenanya.mindsync.ui.screen.main.MainViewModel

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val notesData by mainViewModel.notesData.observeAsState()
    val contentState = mainViewModel.contentState.observeAsState()

    LaunchedEffect(Unit) {
        mainViewModel.getNotes()
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
            Text(
                "Catatan", style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                notesData?.let {
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
        if (contentState.value is ContentState.Loading) {
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

@Composable
fun NoteItem(note_id: Int, notes: String, createdAt: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("note_detail/${note_id}") },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notes", style = MaterialTheme.typography.bodyMedium)
                Text(createdAt.substring(0, 10), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(notes, style = MaterialTheme.typography.bodySmall)
        }
    }
}
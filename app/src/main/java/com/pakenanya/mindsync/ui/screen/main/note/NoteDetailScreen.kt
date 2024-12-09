package com.pakenanya.mindsync.ui.screen.main.note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    navBackStackEntry: androidx.navigation.NavBackStackEntry
) {
    val note_id = navBackStackEntry.arguments?.getString("note_id") ?: "0"

    val viewModel: NoteDetailViewModel = hiltViewModel()

    LaunchedEffect(note_id) {
        viewModel.getNote(id = note_id.toInt())
    }

    val noteData = viewModel.noteData.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Detail") },
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
            noteData.value?.let { Text(it.text) }
        }
    }
}
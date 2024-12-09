package com.pakenanya.mindsync.ui.screen.main.document

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
import com.pakenanya.mindsync.ui.screen.main.note.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    navBackStackEntry: androidx.navigation.NavBackStackEntry
) {
    val document_id = navBackStackEntry.arguments?.getString("document_id") ?: "0"

    val viewModel: DocumentDetailViewModel = hiltViewModel()

    LaunchedEffect(document_id) {
        viewModel.getDocument(document_id.toInt())
    }

    val documentData = viewModel.documentData.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Document Detail") },
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
            documentData.value?.let { Text(it.title) }
        }
    }
}
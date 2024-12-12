package com.pakenanya.mindsync.ui.screen.main.note

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val deleteResult = viewModel.deleteResult.observeAsState()

    var showNoteDialog by remember { mutableStateOf(false) }
    var deleteNoteDialog by remember { mutableStateOf(false) }

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
                },
                actions = {
                    Text(
                        "Edit",
                        modifier = Modifier.clickable { showNoteDialog = true }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("|")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        "Hapus",
                        modifier = Modifier.clickable { deleteNoteDialog = true }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {

            Box(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                noteData.value?.let { Text(it.text) }
            }
        }

        if (showNoteDialog) {
            NoteDialog(
                currentNotes = noteData.value?.text ?: "",
                onConfirm = { newNotes ->
                    viewModel.updateNote(note_id.toInt(), newNotes)
                    showNoteDialog = false
                },
                onDismiss = { showNoteDialog = false }
            )
        }

        if (deleteNoteDialog) {
            DeleteNoteDialog(
                onConfirm = {
                    deleteNoteDialog = false
                    viewModel.deleteNote(note_id.toInt())
                    navController.navigateUp()
                },
                onDismiss = { deleteNoteDialog = false }
            )
        }
    }
}

@Composable
fun NoteDialog(
    currentNotes: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var notes by remember { mutableStateOf(currentNotes) }

    val textFieldStates = remember { mutableStateMapOf<String, Boolean>() }
    fun getBorderColor(key: String): Color {
        return if (textFieldStates[key] == true) Color(0xFF006FFD) else Color(0xFFC5C6CC)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Catatan") },
        text = {
            Column {
                Text("Perbarui catatan Anda untuk pencarian yang lebih akurat")
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan") },
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
                            textFieldStates["note"] = focusState.isFocused
                        }
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = getBorderColor("note")
                            ),
                            shape = RoundedCornerShape(12)
                        ),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(notes) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15104D),
                    contentColor = Color.White
                )
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15104D),
                    contentColor = Color.White
                )
            ) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun DeleteNoteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Peringatan!") },
        text = {
            Text("Anda yakin ingin menghapus catatan?")
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15104D),
                    contentColor = Color.White
                )
            ) {
                Text("Yakin")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15104D),
                    contentColor = Color.White
                )
            ) {
                Text("Batal")
            }
        }
    )
}
package com.pakenanya.mindsync.ui.screen.main

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pakenanya.mindsync.R
import com.pakenanya.mindsync.ui.navigation.Routes
import com.pakenanya.mindsync.ui.screen.auth.AuthState
import com.pakenanya.mindsync.ui.screen.auth.AuthViewModel
import com.pakenanya.mindsync.ui.screen.main.dashboard.DashboardScreen
import com.pakenanya.mindsync.ui.screen.main.document.DocumentScreen
import com.pakenanya.mindsync.ui.screen.main.note.NoteScreen
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val viewModel: MainViewModel = hiltViewModel()

    val mainNavController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    var showDocumentDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }


    val items = listOf(
        BottomNavItem("Dashboard", R.drawable.ic_dashboard, Routes.DASHBOARD),
        BottomNavItem("Document", R.drawable.ic_document, Routes.DOCUMENT),
        BottomNavItem("Notes", R.drawable.ic_note, Routes.NOTES),
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val mainRoute = navBackStackEntry?.destination?.route

    val authState = authViewModel.authState.observeAsState()
    val userData = authViewModel.userData.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.LOGIN)
            else -> Unit
        }
    }

    LaunchedEffect(mainRoute) {
        selectedItem = when (mainRoute) {
            Routes.DASHBOARD -> 0
            Routes.DOCUMENT -> 1
            Routes.NOTES -> 2
            else -> selectedItem
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            if (currentRoute == Routes.MAIN_SCREEN) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Halo ${userData.value?.username ?: "Foo"}!",
                        )
                    },
                    actions = {
                        Image(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navController.navigate(Routes.PROFILE)
                                },
                            contentScale = ContentScale.Crop,
                            painter = painterResource(R.drawable.profile),
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }

                )
            }
        },
        bottomBar = {
            if (currentRoute == Routes.MAIN_SCREEN) {
                NavigationBar(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    ) {
                    items.forEachIndexed { index, item ->
                        val isSelected = selectedItem == index

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                            selected = isSelected,
                            onClick = {
                                selectedItem = index
                                mainNavController.navigate(item.route)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF382ACC),
                                unselectedIconColor = Color(0xFFC7C5DE),
                                selectedTextColor = Color(0xFF382ACC),
                                unselectedTextColor = Color(0xFFC7C5DE),
                                indicatorColor = Color(0xFFC7C5DE),
                            ),
    //                        alwaysShowLabel = false
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Routes.MAIN_SCREEN && (selectedItem == 1 || selectedItem == 2)) {
                FloatingActionButton(
                    onClick = {
                        if(selectedItem == 1) {
                            Log.d("FAB", "Documents: FAB clicked")
                            showDocumentDialog = true
                        } else if (selectedItem == 2) {
                            Log.d("FAB", "Notes: FAB clicked")
                            showNoteDialog = true
                        }
                    },
                    containerColor = Color(0xFF382ACC),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { _ ->
        if (showDocumentDialog) {
            DocumentFilePickerDialog(
                onConfirm = { title, filePart ->
                    viewModel.documentOnSubmitted(file = filePart, title)
                    showDocumentDialog = false
                },
                onDismiss = { showDocumentDialog = false },
                onFileSelected = { filePath ->
                    Log.d("FilePicker", "File selected: $filePath")
                }
            )
        } else if(showNoteDialog) {

            NoteDialog(
                onConfirm = { notes ->
                    viewModel.noteOnSubmitted(text = notes)
                    showNoteDialog = false
                },
                onDismiss = { showNoteDialog = false },
            )
        }

        NavHost(
            navController = mainNavController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.DASHBOARD) {
                DashboardScreen(modifier, navController)
            }
            composable(Routes.DOCUMENT) {
                DocumentScreen(modifier, navController, mainViewModel = viewModel)
            }
            composable(Routes.NOTES) {
                NoteScreen(modifier, navController, mainViewModel = viewModel)
            }
        }
    }
}

@Composable
fun DocumentFilePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, MultipartBody.Part) -> Unit,
    onFileSelected: (MultipartBody.Part) -> Unit
) {
    val textFieldStates = remember { mutableStateMapOf<String, Boolean>() }
    fun getBorderColor(key: String): Color {
        return if (textFieldStates[key] == true) Color(0xFF006FFD) else Color(0xFFC5C6CC)
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val fileDescriptor = context.contentResolver.openFileDescriptor(uri, "r") ?: return null
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val file = File(context.cacheDir, uri.path?.substringAfterLast("/") ?: "temp_file")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return file
    }

    fun createMultipartBodyPart(file: File, partName: String): MultipartBody.Part {
        val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    var fileName by remember { mutableStateOf("") }
    var filePart by remember { mutableStateOf<MultipartBody.Part?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val file = getFileFromUri(context, it)
            if (file != null) {
                fileName = file.name
                filePart = createMultipartBodyPart(file, "file")
                onFileSelected(filePart!!)
            }
        }
    }

    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload Dokumen") },
        text = {
            Column {
                Text("Unggah dokumen tim agar Anda dapat mencarinya di masa mendatang")
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul") },
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
                            textFieldStates["title"] = focusState.isFocused
                        }
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = getBorderColor("title")
                            ),
                            shape = RoundedCornerShape(12)
                        ),
                )
                TextField(
                    value = fileName,
                    onValueChange = {},
                    label = { Text("Pilih File") },
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        Color.Black,
                        cursorColor = Color(0xFF006FFD),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.clickable {
                        launcher.launch("*/*")
                    },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { filePart?.let { onConfirm(title, it) } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF15104D),
                    contentColor = Color.White
                )
            ) {
                Text("Upload")
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
fun NoteDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var notes by remember { mutableStateOf("") }

    val textFieldStates = remember { mutableStateMapOf<String, Boolean>() }
    fun getBorderColor(key: String): Color {
        return if (textFieldStates[key] == true) Color(0xFF006FFD) else Color(0xFFC5C6CC)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buat Catatan") },
        text = {
            Column {
                Text("Buat catatan untuk Anda cari di masa mendatang")
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
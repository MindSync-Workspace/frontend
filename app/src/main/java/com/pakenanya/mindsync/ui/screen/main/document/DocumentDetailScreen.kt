package com.pakenanya.mindsync.ui.screen.main.document

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pakenanya.mindsync.data.remote.response.ChatsData
import com.pakenanya.mindsync.ui.screen.main.document.model.ChatUiModel

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

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Ringkasan", "Percakapan")

    val documentData = viewModel.documentData.observeAsState()
    val chatsData = viewModel.chatsData.observeAsState()

    fun List<ChatsData>.toChatUiModel(): ChatUiModel {
        val addressee = ChatUiModel.Author.ai

        val messages = this.map { chat ->
            ChatUiModel.Message(
                text = chat.text,
                author = if (chat.isHuman) ChatUiModel.Author.me else ChatUiModel.Author.ai
            )
        }

        return ChatUiModel(
            messages = messages,
            addressee = addressee
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TabBar
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(
                                text = title,
                                color = Color(0xFF382ACC)
                            ) },
                        )
                    }
                }

                // Content based on selected tab
                when (selectedTabIndex) {
                    0 -> documentData.value?.summary?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    1 -> ChatScreen(
                        model = chatsData.value!!.toChatUiModel(),
                        onSendChatClickListener = { msg -> viewModel.sendChat(document_id.toInt(), msg) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun ChatScreen(
    model: ChatUiModel,
    onSendChatClickListener: (String) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (messages, chatBox) = createRefs()

        val listState = rememberLazyListState()
        LaunchedEffect(model.messages.size) {
            listState.animateScrollToItem(model.messages.size)
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(messages) {
                    top.linkTo(parent.top)
                    bottom.linkTo(chatBox.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
            contentPadding = PaddingValues(16.dp)
        ) {
            if (model.messages.isNotEmpty()) {
                items(model.messages) { item ->
                    ChatItem(item)
                }
            }
        }

        ChatBox(
            onSendChatClickListener,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun ChatItem(message: ChatUiModel.Message) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
        Box(
            modifier = Modifier
                .align(if (message.isFromHuman) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.isFromHuman) 48f else 0f,
                        bottomEnd = if (message.isFromHuman) 0f else 48f
                    )
                )
                .background(Color(0xFF382ACC))
                .padding(16.dp)
        ) {
            Text(text = message.text, color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(
    onSendChatClickListener: (String) -> Unit,
    modifier: Modifier
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current

    Row(modifier = modifier.padding(16.dp)) {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Tanya sesuatu")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                onSendChatClickListener(chatBoxValue.text)
                focusManager.clearFocus()
            })
        )
        IconButton(
            onClick = {
                val msg = chatBoxValue.text
                if (msg.isBlank()) return@IconButton
                onSendChatClickListener(chatBoxValue.text)
                chatBoxValue = TextFieldValue("")
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFF382ACC))
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier.fillMaxSize().padding(8.dp),
                tint = Color.White
            )
        }
    }
}
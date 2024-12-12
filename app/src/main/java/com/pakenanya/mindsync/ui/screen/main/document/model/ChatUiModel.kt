package com.pakenanya.mindsync.ui.screen.main.document.model

data class ChatUiModel(
    val messages: List<Message>,
    val addressee: Author,
) {
    data class Message(
        val text: String,
        val author: Author,
    ) {
        val isFromHuman: Boolean
            get() = author.id == MY_ID
    }

    data class Author(
        val id: String,
        val name: String
    ) {
        companion object {
            val ai = Author("1", "AI")
            val me = Author(MY_ID, "Me")
        }
    }

    companion object {
        const val MY_ID = "-1"
    }
}
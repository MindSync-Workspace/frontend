package com.pakenanya.mindsync.data.local.room

import androidx.room.*
import com.pakenanya.mindsync.data.remote.response.ChatsData

@Dao
interface ChatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatsData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatsData>)

    @Query("SELECT * FROM chats WHERE documentId = :documentId")
    suspend fun getChatsByDocumentId(documentId: Int): List<ChatsData>

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChatById(chatId: Int)

    @Query("DELETE FROM chats")
    suspend fun clearAllChats()

    @Update
    suspend fun updateChat(chat: ChatsData)
}

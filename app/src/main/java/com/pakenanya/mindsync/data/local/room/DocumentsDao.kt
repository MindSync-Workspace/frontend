package com.pakenanya.mindsync.data.local.room

import androidx.room.*
import com.pakenanya.mindsync.data.remote.response.DocumentsData

@Dao
interface DocumentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentsData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(documents: List<DocumentsData>)

    @Query("SELECT * FROM documents WHERE id = :documentId LIMIT 1")
    suspend fun getDocumentById(documentId: Int): DocumentsData?

    @Query("SELECT * FROM documents WHERE userId = :userId")
    suspend fun getDocumentsByUserId(userId: Int): List<DocumentsData>

    @Update
    suspend fun updateDocument(document: DocumentsData)

    @Query("DELETE FROM documents WHERE id = :documentId")
    suspend fun deleteDocumentById(documentId: Int)

    @Query("DELETE FROM documents")
    suspend fun clearAllDocuments()
}

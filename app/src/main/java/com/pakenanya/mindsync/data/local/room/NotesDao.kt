package com.pakenanya.mindsync.data.local.room

import androidx.room.*
import com.pakenanya.mindsync.data.remote.response.NotesData

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NotesData>)

    @Query("SELECT * FROM notes WHERE id = :noteId LIMIT 1")
    suspend fun getNoteById(noteId: Int): NotesData?

    @Query("SELECT * FROM notes WHERE userId = :userId")
    suspend fun getNotesByUserId(userId: Int): List<NotesData>

    @Query("SELECT * FROM notes WHERE orgId = :orgId")
    suspend fun getNotesByOrgId(orgId: Int): List<NotesData>

    @Query("SELECT * FROM notes WHERE userId = :userId AND orgId = :orgId")
    suspend fun getNotesByUserAndOrg(userId: Int, orgId: Int): List<NotesData>

    @Update
    suspend fun updateNote(note: NotesData)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()
}
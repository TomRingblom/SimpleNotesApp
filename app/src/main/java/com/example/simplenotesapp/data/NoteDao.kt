package com.example.simplenotesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * from notes ORDER BY id ASC")
    fun getAllNotes(): Flow<List<Note>>
}
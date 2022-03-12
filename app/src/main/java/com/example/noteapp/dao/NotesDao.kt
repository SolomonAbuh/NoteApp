package com.example.noteapp.dao

import androidx.room.*
import androidx.room.Dao
import com.example.noteapp.entities.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    suspend fun getAllNote():List<Notes>

    @Query("SELECT * FROM Notes WHERE id =:id")
    suspend fun getSpecificNote(id:Int): Notes


//    @Query("DELETE FROM Notes")
//    suspend fun deleteAllNotes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: Notes)

    @Delete
    suspend fun deleteNotes(note: Notes)


}
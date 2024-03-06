package com.futdomingo.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.futdomingo.Player

@Dao
interface PlayerDAO {

    @Query("SELECT * FROM players")
    fun liveDataGetAll() : LiveData<List<Player>>

    @Query("SELECT * FROM players")
    fun getAll() : List<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(player: Player)

    @Query("DELETE FROM players WHERE name = :player")
    fun delete(player: String)



}
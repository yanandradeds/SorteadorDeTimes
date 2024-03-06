package com.futdomingo.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.futdomingo.Player
import com.futdomingo.repository.AppDatabase

class TeamsDrawnViewModel(context: Context) : ViewModel() {

    private val databaseDAO = AppDatabase.getDatabase(context).playerDAO()
    fun saveData(name: String){
        databaseDAO.insert(Player(name = name))
    }
    fun getAll() = databaseDAO.getAll()

    var liveDataPlayers = databaseDAO.liveDataGetAll()

    fun delete(player: String) = databaseDAO.delete(player)

}
package com.futdomingo.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TeamsDrawnViewModel : ViewModel() {

    var teamsDrawn = MutableLiveData<List<String>>(listOf())

}
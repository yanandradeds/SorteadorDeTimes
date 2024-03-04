package com.futdomingo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.futdomingo.ViewModel.TeamsDrawnViewModel


@Composable
fun DrawnTeams(navController: NavController?, viewModel: TeamsDrawnViewModel){

    val teams = remember {
        mutableStateListOf<String>()
    }

    teams.addAll(drawnTeams(viewModel))

    Column(Modifier.fillMaxSize()) {
        for (team in teams.toList()) {

            Surface(Modifier.fillMaxWidth(), shadowElevation = 4.dp) {
                Text(text = team)
            }
        }
    }


}

private fun drawnTeams(viewModel: TeamsDrawnViewModel): ArrayList<String> {
    val mTeams = arrayListOf<String>()
    val toDrawTeams = viewModel.players.value!!

    while (toDrawTeams.isNotEmpty()) {
        var buildingTeam = ""

        repeat(5) {
            if(toDrawTeams.isNotEmpty()) {
                val selectedPlayer = (toDrawTeams.indices).random()

                buildingTeam += toDrawTeams[selectedPlayer] + ", "
                toDrawTeams.removeAt(selectedPlayer)
            }
        }

        mTeams.add("${mTeams.size+1} - $buildingTeam")
    }

    return mTeams
}
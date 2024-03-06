package com.futdomingo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import androidx.navigation.NavController
import com.futdomingo.ViewModel.TeamsDrawnViewModel


@Composable
fun DrawnTeams(navController: NavController?, viewModel: TeamsDrawnViewModel){

    val teams = remember {
        mutableStateMapOf<Int,String>()
    }

    viewModel.liveDataPlayers.observe(LocalLifecycleOwner.current) { list ->
        teams.putAll(drawnTeams(list.map {it.name}))
    }



    Column(Modifier.fillMaxSize()) {
        for (i in 1..teams.size) {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp, 6.dp),
                shadowElevation = 4.dp, shape = RoundedCornerShape(4.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Text(text = "Time $i")
                    }
                    val split = teams[i]!!.split(",")

                    for (player in split) Text(text = player, Modifier.padding(0.dp,2.dp))
                }

            }

        }
    }


}

private fun drawnTeams(players: List<String>): Map<Int,String> {
    val mTeams = mutableMapOf<Int,String>()
    val arrayListPlayers = arrayListOf<String>()
    arrayListPlayers.addAll(players)


    for(i in 1..(arrayListPlayers.size/5)) {
        for (x in 1..5) {
            if (arrayListPlayers.isNotEmpty()) {
                val selectedPlayer = (arrayListPlayers.indices).random()
                if (x == 1) mTeams[i] = ""
                mTeams[i] = "${mTeams[i]}${arrayListPlayers[selectedPlayer]}"
                arrayListPlayers.removeAt(selectedPlayer)
                if (x != 5) mTeams[i] += ","

            } else {
                break
            }
        }
    }

    return mTeams
}

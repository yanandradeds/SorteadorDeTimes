package com.futdomingo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.futdomingo.ViewModel.TeamsDrawnViewModel


@Composable
fun DrawnTeams(navController: NavController?, viewModel: TeamsDrawnViewModel?){

    val teams = remember {
        mutableStateListOf<String>()
    }

    viewModel?.teamsDrawn?.observeAsState().let {
        teams.addAll(viewModel?.teamsDrawn?.value!!)
    }

    Box(Modifier.fillMaxSize()) {

        if (!teams.isEmpty() && teams.size > 9) {

            val numberOfTeams = when (teams.size) {
                in 0..5 -> 1
                in 6..10 -> 2
                in 11..15 -> 3
                in 15..20 -> 4
                else -> throw IndexOutOfBoundsException()

            }

            val arrayTeams = arrayListOf<String>()
            var buildString = ""

            for (i in 1..numberOfTeams) {
                repeat(5) {
                    if (teams.isNotEmpty()) {
                        buildString += teams.first() + ", "
                        teams.removeAt(0)
                    }
                }
                arrayTeams.add(buildString)

                Text(text = "$i - ${arrayTeams[i - 1]}", modifier = Modifier.offset(10.dp,(18*i).dp))
            }

        }


    }


}


package com.futdomingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection.Companion.In
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.futdomingo.ViewModel.TeamsDrawnViewModel
import com.futdomingo.ui.theme.FutDomingoTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContent {
            FutDomingoTheme {
                Surface {
                    val navController: NavHostController = rememberNavController()
                    val viewModel = TeamsDrawnViewModel()

                    NavHost(navController = navController, startDestination = "mainPage") {
                        composable("mainPage") {
                            App(navController, viewModel)
                        }
                        composable("drawnTeamsPage") {
                            DrawnTeams(navController, viewModel)

                        }
                    }

                }
            }
        }


    }
}

@Composable
fun App(mNavController: NavController?, drawnViewModel: TeamsDrawnViewModel?)
{
    val players = remember { mutableStateListOf<String>() }
    val showMiddleComponents = remember { mutableStateOf(false)}
    val navController = mNavController!!

    Box(modifier = Modifier
        .fillMaxSize(1f)
        .background(Color.White)
        ) {


        TopTextField(Modifier.align(Alignment.TopCenter))
        PlayersName(players)
        DrawButton(teams = players,
            onClickEvent = {
                playersToDraw ->
                drawnViewModel?.teamsDrawn?.value = drawTeams(playersToDraw)
                navController.navigate("drawnTeamsPage")},
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController)

        InputCenterComponents(show = showMiddleComponents.value,
            inputPlayers = { players.add(it) },
            Modifier.align(Alignment.Center))

        BottomFAB(show = showMiddleComponents.value,
            onClick = { input -> showMiddleComponents.value = input },
            modifier = Modifier.align(Alignment.BottomEnd))


    }

}

@Composable
fun TopTextField(modifier: Modifier) {
    Text(modifier = modifier.padding(0.dp, 0.dp, 0.dp, 20.dp),
        text = "Lista de Jogadores")
}

@Composable
fun PlayersName(playerList: List<String>) {
    var position = 1

    for (player in playerList) {
        Text(text = "$position - $player", modifier = Modifier.offset(10.dp,(18*position).dp))
        position++
    }

}

@Composable
fun DrawButton(teams: List<String>,
               onClickEvent: (List<String>) -> Unit,
               modifier: Modifier,
               navController: NavController) {
    Button(onClick = {
        onClickEvent(teams)
    },
        modifier = modifier.offset(0.dp, (-80).dp),
        shape = RoundedCornerShape(8.dp)) {
        Text(text = "Sortear")
    }
}

@Composable
fun InputCenterComponents(show: Boolean, inputPlayers: (String) -> Unit, modifier: Modifier) {

    val typedText = remember { mutableStateOf("") }
    var invalidCharacters by remember {  mutableStateOf(false) }
    var count  by remember { mutableIntStateOf(0) }
    val limit = 20





     if (show) {


             Surface(modifier = modifier.wrapContentSize(), shadowElevation = 8.dp) {
                Column {
                    OutlinedTextField(
                        value = typedText.value,
                        onValueChange = { typedText.value = it
                            invalidCharacters = isInvalidInput(it)
                            count = it.length},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrect = false),
                        label = { Text(text = "Nome")},
                        isError = invalidCharacters,
                        trailingIcon = { if(invalidCharacters) Icon(Icons.Filled.Info,  "") },
                        singleLine = true,
                        supportingText = {
                            if(invalidCharacters) Text("Dígito inválido", modifier = Modifier.align(Alignment.End))
                            else Text("$count/$limit") }
                    )

                    Button(
                        onClick = { if(!invalidCharacters) {
                            inputPlayers(typedText.value)
                            typedText.value = "" }
                        }

                    ) {
                        Text(text = "Inserir")
                    }
                }

             }





     }

}

@Composable
fun BottomFAB(show: Boolean, onClick : (Boolean) -> Unit, modifier: Modifier) {

    FloatingActionButton(modifier = modifier.padding(0.dp,0.dp, 16.dp, 16.dp),
        onClick = { onClick(!show)} ) {

        val icon = when(show) {
            true -> Icons.Filled.Clear
            false -> Icons.Filled.Add
        }

        Icon(icon , contentDescription = "Plus icon/Less icon")
    }

}

fun drawTeams(playerList: List<String>): List<String> {
    val oldList: MutableList<String> = mutableListOf()
    val drawnList: ArrayList<String> = arrayListOf()
    oldList.addAll(playerList)

    while (oldList.size != 0) {
        val position = (0..< oldList.size).random()
        drawnList.add(oldList[position])
        oldList.removeAt(position)
    }

    return drawnList
}

private fun isInvalidInput(input: String): Boolean {
    val pattern = "[^a-zA-z]"
    val regex = Regex(pattern)

    input.forEach {
        if (regex.matches(it.toString())) return true
    }

    return false
}
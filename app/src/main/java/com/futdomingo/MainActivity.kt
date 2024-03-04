package com.futdomingo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat.ThemeCompat
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
                Surface(Modifier.fillMaxSize()) {
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
fun App(navController: NavController, drawnViewModel: TeamsDrawnViewModel)
{
    val players = remember { mutableStateListOf<String>() }
    var showMiddleComponents by remember { mutableStateOf(false)}
    val context = LocalContext.current
    val finalList: ArrayList<String> = arrayListOf()


    Column(modifier = Modifier.fillMaxSize(1f)) {
        Text(text = "Lista de jogadores",
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 20.dp))

        Box(modifier = Modifier
            .weight(10f)
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(0.dp, 4.dp)){

            Surface(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)) {

                LazyColumn() {

                    itemsIndexed(players.toList()) {index, player ->
                        Surface(modifier = Modifier
                            .fillMaxWidth().padding(4.dp,4.dp)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(2.dp)),
                            shadowElevation = 6.dp
                            ) {
                            Row(modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(2.dp))) {
                                Text("${index+1} - $player", modifier = Modifier.padding(10.dp,0.dp))
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "",
                                    tint = Color.Red, modifier = Modifier.padding(10.dp,0.dp))
                            }

                        }

                    }
                }
                
            }

            InputCenterComponents(
                show = showMiddleComponents,
                inputPlayers = { players.add(it) },
                modifier = Modifier
                    .align(Alignment.BottomCenter))

        }

        Box(
            Modifier
                .weight(1.25f)
                .fillMaxWidth()) {
            Button(
                onClick = {
                    if (players.size > 9) {
                        for (p in players) {
                            finalList.add(p)
                        }
                        drawnViewModel.players.value = finalList
                        navController.navigate("drawnTeamsPage")
                    } else {
                        Toast.makeText(
                            context,
                            "Minimo de 10 jogadores para sorteio",
                            Toast.LENGTH_LONG).show()
                    }
                          },
                modifier = Modifier.align(Alignment.Center),
                shape = RoundedCornerShape(8.dp)) {
                Text(text = "Sortear")
            }

            BottomFAB(show = showMiddleComponents,
                onClick = {input -> showMiddleComponents = input},
                modifier = Modifier.align(Alignment.BottomEnd))
        }


    }

}






@Composable
fun InputCenterComponents(show: Boolean, inputPlayers: (String) -> Unit, modifier: Modifier) {

    var typedText by remember { mutableStateOf("") }
    var invalidCharacters by remember {  mutableStateOf(false) }

    Surface(
        modifier = modifier
            .wrapContentSize()
            .padding(0.dp, 16.dp),
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(10.dp)) {

        if (show) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Adicionar novo jogadores",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Start)

                OutlinedTextField(
                    modifier = Modifier.padding(16.dp,0.dp),
                    value = typedText,
                    onValueChange = {
                        if(it.length <= 20) typedText = it
                        invalidCharacters = isInvalidInput(it)
                        if(it == " ") typedText = "" },
                    label = { Text(text = "Nome")},
                    isError = invalidCharacters,
                    trailingIcon = { if(invalidCharacters) Icon(Icons.Filled.Info,  "") },
                    singleLine = true,
                    supportingText = {
                        if(invalidCharacters) Text("Dígito inválido", modifier = Modifier.align(Alignment.End)) }
                )

                Button(
                    onClick = { if(!invalidCharacters && typedText.isNotEmpty()) {
                        inputPlayers(typedText)
                        typedText = "" }
                    },
                    modifier = Modifier.padding(0.dp,10.dp),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Text(text = "Inserir")
                }
            }
        }
 }

}

@Composable
fun BottomFAB(show: Boolean, onClick : (Boolean) -> Unit, modifier: Modifier) {

    FloatingActionButton(modifier = modifier.padding(0.dp,0.dp,10.dp,10.dp),
        onClick = { onClick(!show)},
        ) {

        val icon = when(show) {
            true -> Icons.Filled.Clear
            false -> Icons.Filled.Add
        }

        Icon(icon , contentDescription = "Plus icon/Less icon")
    }

}

private fun isInvalidInput(input: String): Boolean {
    val pattern = "[^a-zA-z]".toRegex()
    val accents = "[^áàâãéèêíïóôõöúçñ]".toRegex()
    val whiteSpace = "\\s".toRegex()

    var isInvalid = false

    for(letter in input) {
        isInvalid = pattern.matches(letter.toString())

        if (pattern.matches(letter.toString())) isInvalid = accents.matches(letter.toString())
        if (whiteSpace.matches(letter.toString())) isInvalid = false

        if(isInvalid) break
    }

    return isInvalid
}
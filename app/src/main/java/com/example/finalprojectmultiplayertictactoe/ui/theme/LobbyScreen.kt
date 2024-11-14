package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.finalprojectmultiplayertictactoe.GameViewModel
import com.example.finalprojectmultiplayertictactoe.Player

import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.*


@Composable
fun LobbyScreen(navController: NavController, gameViewModel: GameViewModel){

    val player1Name = gameViewModel.player1Name.value
    var players by remember { mutableStateOf<List<Player>>(emptyList()) }


    LaunchedEffect(Unit){
        gameViewModel.fetchPlayers {fetchedPlayers ->
            players = fetchedPlayers
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ){
        Text(text = "Lobby", fontSize = 42.sp)
        Text(text = "Players in the lobby:", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "$player1Name", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(8.dp))

        players.forEach { player ->
            Text(text = player.name, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(text = gameViewModel.player2Name.value ?: "Waiting for player 2...", fontSize = 24.sp)

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                gameViewModel.resetGame()
                navController.navigate("game")
            }){
                Text(text = "Invite to a challenge")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("challengeRequests") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .width(300.dp)
                .height(60.dp)
        ){
            Text(text = "Challenge requests", fontSize = 18.sp)
        }
    }
}


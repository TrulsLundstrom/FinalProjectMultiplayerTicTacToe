package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.finalprojectmultiplayertictactoe.GameViewModel


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.lifecycle.compose.collectAsStateWithLifecycle




@Composable
fun LobbyScreen(navController: NavController, gameViewModel: GameViewModel){
    val players by gameViewModel.players.collectAsStateWithLifecycle()
    val currentPlayerId by gameViewModel.playerDocumentId.collectAsStateWithLifecycle(initialValue = null)
    val challengeRequestCount by gameViewModel.challengeRequestCount.collectAsStateWithLifecycle(initialValue = 0)

    LaunchedEffect(Unit){
        gameViewModel.listenToLobbyPlayers()
    }

    LaunchedEffect(currentPlayerId){
        if(currentPlayerId != null){
            gameViewModel.listenToChallengeRequests()
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

        players.forEach { player ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = player.name, fontSize = 24.sp)

                if(currentPlayerId != null && player.playerId != currentPlayerId){
                    Button(onClick = {
                        gameViewModel.sendChallenge(senderId = currentPlayerId!!, receiverId = player.playerId)
                    }){
                        Text(text = "Invite to a challenge")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "You have $challengeRequestCount challenge request(s)",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

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


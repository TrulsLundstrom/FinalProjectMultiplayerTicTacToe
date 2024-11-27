package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight

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
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Lobby",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Players in the lobby:",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        players.forEach { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = player.name,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if(currentPlayerId != null && player.playerId != currentPlayerId){
                        Button(onClick = {
                            gameViewModel.sendChallenge(senderId = currentPlayerId!!, receiverId = player.playerId, navController = navController) // kompileringsfel: " No value passed for parameter 'navController' "
                        }){
                            Text(text = "Challenge")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "You have $challengeRequestCount challenge request(s)",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { navController.navigate("challengeRequests") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(60.dp)
        ){
            Text(text = "View Challenge Requests", fontSize = 18.sp)
        }
    }
}


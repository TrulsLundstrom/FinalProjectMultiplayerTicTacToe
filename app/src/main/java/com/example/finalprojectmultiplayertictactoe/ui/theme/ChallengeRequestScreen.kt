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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalprojectmultiplayertictactoe.GameViewModel

@Composable
fun ChallengeRequestScreen(navController: NavController, gameViewModel: GameViewModel){
    val challenges by gameViewModel.challenges.collectAsState()
    val players by gameViewModel.players.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(text = "Challenge requests", fontSize = 30.sp)

        Spacer(modifier = Modifier.height(16.dp))

        challenges.forEach { challenge ->
            val senderName = players.find { it.playerId == challenge.senderId }?.name ?: "Unknown"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = senderName, fontSize = 18.sp)

                Row{
                    Button(
                        onClick = { gameViewModel.respondToChallenge(challenge, accept = true) },
                        modifier = Modifier.padding(end = 8.dp)
                    ){
                        Text("Accept")
                    }

                    Button(
                        onClick = { gameViewModel.respondToChallenge(challenge, accept = false) }
                    ){
                        Text("Decline")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("lobby") },
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Text(text = "Return to lobby", fontSize = 18.sp)
        }
    }
}


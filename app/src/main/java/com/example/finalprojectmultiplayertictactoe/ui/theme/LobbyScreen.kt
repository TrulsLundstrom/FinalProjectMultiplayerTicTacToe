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

    // hämtar en lista med spelare i lobbyn
    val players by gameViewModel.players.collectAsStateWithLifecycle()

    // hämtar den nuvarande spelarens dokument id
    val currentPlayerId by gameViewModel.playerDocumentId.collectAsStateWithLifecycle(initialValue = null)

    // hämtar värdet på en räknare som räknar antalet utmaningar en spelare har
    val challengeRequestCount by gameViewModel.challengeRequestCount.collectAsStateWithLifecycle(initialValue = 0)

    // lyssnar på ändringar i lobbyns spelare
    LaunchedEffect(Unit){ // Unit => körs ENDAST EN GÅNG, vilket är när LobbyScreen först skapas
        gameViewModel.listenToLobbyPlayers()
    }

    // lyssnar på utmanings förfrågningar OM spelaren har ett existerande ID
    LaunchedEffect(currentPlayerId){ // koden nedan körs då currentPlaterId förändras
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

        // loopar igenom alla spelare i lobbyn och visar dem som kort (forEach går igenom alla players objekt, genom att skicka in players till inparametern player)
        // players är listan med Player objekt. players.forEach gör så att man itererar över allt i listan. Inparametern player representerar ETT objekt från listan under varje varv
        // detta kan jämföras med att iterera igenom en array
        players.forEach { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp), //
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


                    // man ska inte kunna bjuda in sig själv, därför visas en knapp "Challenge" för andra spelare men inte vid sitt egna namn.
                    if(currentPlayerId != null && player.playerId != currentPlayerId){
                        Button(onClick = {
                            gameViewModel.sendChallenge(senderId = currentPlayerId!!, receiverId = player.playerId, navController = navController)
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

/*
förklaring varför man kan jämföra playerId och currentPlayerId (nuvarande spelares document ID)


Document ID och fields är relaterade till en specifik spelare.
Man kan därför jämföra fields och document ID och man kan kontrollera om de båda är relaterade till samma spelare eller inte, genom t.ex en if-sats.

forEach loopen går igenom alla spelare i lobbyn och i if-satsten så kontrolleras om dokument ID (currentPlayerId) och den nuvarande playerId är båda relaterade till samma spelare.
Om de INTE är relaterade till samma spelare så ska "Challenge" knappen visas på skärmen. Om de ÄR relaterade till samma spelare så ska den inte visas.

jag skulle även kunna jämför spelarnas namn och jämföra med document ID, men det finns ju en risk att spelarna har samma namn.

 */

package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalprojectmultiplayertictactoe.GameViewModel

@Composable
fun ChallengeRequestScreen(navController: NavController, gameViewModel: GameViewModel){

    // hämtar challenges från GameViewModel
    val challenges by gameViewModel.challenges.collectAsState() // listan med utmaningar
    val players by gameViewModel.players.collectAsState() // listan med spelare

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp), // fyller hela skärmen. 16 dp mellanrum från skärmkanten
        horizontalAlignment = Alignment.CenterHorizontally, // centrerar inehållet horisontellt, dvs i mitten av dess bred
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Challenge Requests",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // forEach går igenom varje element (i detta fall objekt) i listan challenges, skickar elementet till inparametern challenge
        challenges.forEach { challenge -> val senderName = players.find { it.playerId == challenge.senderId }?.name ?: "Unknown"

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = senderName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row{
                        Button(
                            onClick = {
                                gameViewModel.respondToChallenge(challenge, accept = true, navController = navController)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ){
                            Text("Accept")
                        }

                        Button(
                            onClick = {
                                gameViewModel.respondToChallenge(challenge, accept = false)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ){
                            Text("Decline")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("lobby") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Lobby",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Return to Lobby", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/*
förklaring av lambda uttryck

challenges.forEach { challenge -> val senderName = players.find { it.playerId == challenge.senderId }?.name ?: "Unknown"

inparameter -> funktionens kropp

man ska ju skicka in saker till inparametern, men till skillnad från vanliga funktioner där du själv skickar in argument, så är det den OMGIVANDE FUNKTIONEN
(i detta fall forEach) som skickar in värdena.

challenges.forEach säger: "för varje objekt i listan challenges, anropa den här lambda funktionen och skicka in objekt"

challenges är en lista med Challenge objekt. , challenges.forEach tar autoamtiskt varje objekt i listan och skickar Challenge objektet till argumentet.

det är alltså forEach som anropar lambda funktionen och skickar in objekten Challenge som finns i challenges listan.


lista.forEach { listans element -> gör något med elementet



funktioner som forEach, find och map går igenom varje element i en lista

map omvandrar varje element i en lista eller samling till något annat. Lambda uttrycket definierar hur varje element ska omvandlas.
Den skapar en ny lista med de nya omvandlade elementen.

find tar ett lambda uttryck som definierar ett vilkor. Den går igenom varje element i listan
och när den hittar ett element som uppfyller villkoret, returnerar den (BARA) det första elementet som uppyllde vilkoret.
Om inget element uppfyller villkoret så returneras null.

kortfattat:

find: hittar ett specifikt element som matchar ett vilkor. Den returnerar deet första elementet som uppfyller vilkoret.
map: skapar en ny lista genom att omvandla varje element i den ursprungliga listan.

 */


/*
förklaring av it:

"it" används som ett standardnamn för att referera till det aktuella elementet i ett lambda uttryck, när ingen inparameter anges.
exempepelvis så kan du skriva i din forEach som var:

challenges.forEach { challenge -> val senderName = players.find { it.playerId == challenge.senderId }?.name ?: "Unknown"

till detta:

challenges.forEach { challenge -> val senderName = players.find { spelare-> spelare.playerId == challenge.senderId }?.name ?: "Unknown"

du kan tänka att när "it" används vid players.find inne i { } , så representerar "it" ett element (åt gången) i players listan vid players.find

 dvs "it" representerar players vid players.find
 */
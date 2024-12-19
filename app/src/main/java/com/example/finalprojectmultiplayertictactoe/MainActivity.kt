package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.finalprojectmultiplayertictactoe.ui.theme.ChallengeRequestScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.LobbyScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.ResultScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.GameScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen





// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){
    private val gameViewModel: GameViewModel by viewModels() // skapar en instans av GameViewModel
    private var playerDocumentId: String? = null // lagrar spelarens dokument ID. String eller null

    override fun onCreate(savedInstanceState: Bundle?){ // körs när appen startar, används för att sätta upp allt som behövs för att visa skärmen för användaren
        super.onCreate(savedInstanceState)

        setContent{ // sätter upp hela innehållet på skärmen m.h.a jetpack compose

            val navController = rememberNavController() // rememberNavController() skapar en kontroller för att hantera navigering mellan olika skärmar i appen
            val playerDocumentIdState = gameViewModel.playerDocumentId.collectAsState() // hämtar spelarens dokument ID från ViewModel. ViewModel är en plats där appen kan lagra och hantera data så att den kan användas på olika skärmar i appen

            playerDocumentId = playerDocumentIdState.value // Variabel för att lagra spelarens dokument ID. Den används för att identifiera vilken spelare som har anslutit till lobbyn

            playerDocumentId?.let { id ->
                gameViewModel.listenToChallenges(id) // appen lyssnar på om det finns nya utmaningar
            }

            MaterialTheme( // färgtemat för appen
                colorScheme = lightColorScheme(
                    primary = Color(0xFF003366),
                    secondary = Color(0xFF66AAFF),
                    background = Color(0xFFF5F5F5),
                    surface = Color(0xFFFFFFFF),
                    onPrimary = Color(0xFFFFFFFF),
                    onSecondary = Color(0xFF333333),
                    onBackground = Color(0xFF333333),
                    onSurface = Color(0xFF333333)
                )
            ){
                // NavHost skapar en container för navigering (container . Här definieras alla de skärmar som användarna kan gå mellan.
                NavHost(navController = navController, startDestination = "nameInput"){
                    composable("nameInput"){ // skärm för att skriva sitt namn (start skärmen)
                        PlayerNameInputScreen(
                            gameViewModel = gameViewModel,
                            onContinue = { // när användaren fortsätter, så navigeras man till "lobby" skärmen
                                navController.navigate("lobby")
                            }
                        )
                    }

                    // composable används för att skapa varje skärm i appen. I @Composable (som anropas nedanför) definieras vad som ska hända i varje skärm
                    composable("lobby"){ // lobby skärm
                        LobbyScreen(navController = navController, gameViewModel = gameViewModel)
                    }

                    composable("game"){ // skärm för spelbrädan
                        GameScreen(navController = navController, gameViewModel = gameViewModel)
                    }

                    composable("result"){ // skärm för resultatet
                        val resultMessage by gameViewModel.resultMessage.collectAsState()

                        ResultScreen(resultMessage = resultMessage, navController = navController){

                            // återställer spelet
                            gameViewModel.resetGame()

                            // denna navigering lägger "lobby" på back-stacken ENDAST om den INTE redan finns där.
                            // Eftersom stacken oftast rensas i ResultScreen så innebär detta att "lobby" är den enda skärmen kvar
                            navController.navigate("lobby") // sätter "lobby" skärmen på back stacken men NAVIGERAR INTE.
                        }
                    }

                    composable("challengeRequests"){ // skärmen med utmaningarna
                        ChallengeRequestScreen(navController = navController, gameViewModel = gameViewModel)
                    }
                }
            }
        }
    }

    // anropas när appen stängs av. Jag använder den för att ta bort spelare lobbyn, de har ju avslutat appen och ska därför inte finnas kvar i Firestore och i lobbyn
    override fun onStop(){
        super.onStop()
        playerDocumentId?.let { documentId -> // om det finns ett dokument ID för spelaren...
            gameViewModel.deletePlayerFromLobby(documentId){ isSuccess -> // ta bort spelaren från lobbyn
                println(if(isSuccess) "player document deleted sucessfully" else "failed to delete document")
            }
        }
    }
}

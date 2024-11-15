package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.finalprojectmultiplayertictactoe.ui.theme.ChallengeRequestScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.LobbyScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.ResultScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.GameScreen
import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen
import java.util.concurrent.CountDownLatch


data class Player(
    val playerId: String = "",
    var name: String = "",
    var invitation: String = ""
)


// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){
    private val gameViewModel: GameViewModel by viewModels()
    private var playerDocumentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            val navController = rememberNavController()
            gameViewModel.playerDocumentId.observe(this){ documentId ->
                playerDocumentId = documentId
            }

            NavHost(navController = navController, startDestination = "nameInput"){
                composable("nameInput"){
                    PlayerNameInputScreen(
                        gameViewModel = gameViewModel,
                        onContinue = {
                            navController.navigate("lobby")
                        }
                    )
                }

                composable("lobby"){
                    LobbyScreen(navController = navController, gameViewModel = gameViewModel)
                }

                composable("game"){
                    GameScreen(navController = navController, gameViewModel = gameViewModel)
                }

                composable("result"){
                    ResultScreen(resultMessage = gameViewModel.resultMessage.value, navController = navController){
                        gameViewModel.resetGame()
                        navController.navigate("lobby")
                    }
                }

                composable("challengeRequests"){
                    ChallengeRequestScreen(navController = navController)
                }
            }
        }
    }

    override fun onStop(){
        super.onStop()
        playerDocumentId?.let { documentId ->
            val countDownLatch = CountDownLatch(1)
            gameViewModel.deletePlayerFromLobby(documentId){ isSuccess ->
                countDownLatch.countDown()
                if(isSuccess){
                    println("PLAYER DOCUMENT DELETED SUCCESSFULLY")
                }
                else{
                    println("FAILED TO DELETE PLAYER DOCUMENT")
                }
            }
        }
    }
}



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


data class Player(
    val playerId: String = "",
    var name: String = "",
    var invitation: String = ""
)


// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "nameInput"){
                composable("nameInput"){
                    PlayerNameInputScreen { enteredName ->
                        gameViewModel.setPlayer1Name(enteredName)
                        navController.navigate("lobby")
                    }
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
}



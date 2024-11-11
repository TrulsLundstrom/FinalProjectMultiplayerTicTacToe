package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.runtime.getValue
import androidx.compose.material3.Text

import com.example.finalprojectmultiplayertictactoe.ui.theme.GameBoard
import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finalprojectmultiplayertictactoe.ui.theme.ResultScreen


// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            val navController = rememberNavController()

            val player1Name by gameViewModel.player1Name
            val currentPlayer by gameViewModel.currentPlayer

            val boardState by gameViewModel.boardState
            val resultMessage by gameViewModel.resultMessage

            NavHost(navController = navController, startDestination = "nameInput"){
                composable("nameInput"){
                    PlayerNameInputScreen { enteredName ->
                        gameViewModel.setPlayer1Name(enteredName)
                        navController.navigate("lobby")
                    }
                }

                composable("lobby"){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "Lobby (NOTHING HERE YET)", fontSize = 24.sp)
                        Button(
                            onClick = { navController.navigate("game") },
                            modifier = Modifier.padding(top = 16.dp)
                        ){
                            Text(text = "Start game")
                        }
                    }
                }

                composable("game"){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = if(currentPlayer == "X") "$player1Name's turn" else "${gameViewModel.player2Name}'s turn",
                            fontSize = 42.sp,
                            modifier = Modifier.padding(top = 128.dp)
                        )

                        GameBoard(
                            boardState = boardState,
                            onCellClick = { x, y ->
                                gameViewModel.makeMove(x, y)
                                if(resultMessage != null){
                                    navController.navigate("result")
                                }
                            }
                        )
                    }
                }

                composable("result"){
                    ResultScreen(resultMessage = resultMessage, navController = navController){
                        gameViewModel.resetGame()
                        navController.navigate("lobby")
                    }
                }
            }
        }
    }
}



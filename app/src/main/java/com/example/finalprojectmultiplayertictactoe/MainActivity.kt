package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.runtime.getValue
import androidx.compose.material3.Text

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.finalprojectmultiplayertictactoe.ui.theme.GameBoard
import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

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
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            val navController = rememberNavController()

            var player1Name by remember { mutableStateOf<String?>(null) }
            val player2Name = "Player 2" // placeholder-namn för spelare 2
            var currentPlayer by remember { mutableStateOf("X") }

            var boardState by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
            val gameLogic = GameLogic()
            var resultMessage by remember { mutableStateOf<String?>(null) }

            NavHost(navController = navController, startDestination = "nameInput"){
                composable("nameInput"){

                    if(player1Name == null){
                        PlayerNameInputScreen { enteredName ->
                            player1Name = enteredName
                        }
                    }
                    else{
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = if(currentPlayer == "X") "$player1Name's turn" else "$player2Name's turn",
                                fontSize = 42.sp,
                                modifier = Modifier.padding(top = 128.dp)
                            )

                            GameBoard(
                                boardState = boardState,
                                onCellClick = { x, y ->
                                    if(boardState[x][y] == null && resultMessage == null){
                                        boardState = boardState.copyOf().apply { this[x][y] = currentPlayer }

                                        if(gameLogic.checkWinner(boardState, currentPlayer)){
                                            resultMessage = if(currentPlayer == "X") "$player1Name has won!" else "$player2Name has won!"
                                            navController.navigate("result")
                                        }
                                        else if(boardState.all { row -> row.all { it != null } }){
                                            resultMessage = "It's a draw!"
                                            navController.navigate("result")
                                        }
                                        else{
                                            currentPlayer = if(currentPlayer == "X") "O" else "X"
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                composable("result"){
                   ResultScreen(resultMessage = resultMessage, navController = navController)
                }

                composable("lobby"){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "Lobby (NOTHING HERE YET)", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}



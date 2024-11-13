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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finalprojectmultiplayertictactoe.ui.theme.ResultScreen


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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(text = "Lobby", fontSize = 42.sp)
                        Text(text = "Players in the lobby:", fontSize = 30.sp)

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(text = "$player1Name", fontSize = 24.sp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = gameViewModel.player2Name, fontSize = 24.sp)

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(onClick = { navController.navigate("game") }){
                                Text(text = "Invite to a challenge")
                            }
                        }

                       Spacer(modifier = Modifier.weight(1f))

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

                composable("challengeRequests"){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(text = "Challenge requests", fontSize = 30.sp)

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
            }
        }
    }
}



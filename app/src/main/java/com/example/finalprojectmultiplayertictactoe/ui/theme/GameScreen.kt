package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.finalprojectmultiplayertictactoe.GameViewModel


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){

    val player1Name = gameViewModel.player1Name.value
    val player2Name = gameViewModel.player2Name
    val currentPlayer = gameViewModel.currentPlayer.value

    val boardState = gameViewModel.boardState
    val resultMessage = gameViewModel.resultMessage

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
            boardState = boardState.value,
            onCellClick = { x, y ->
                val previousResult = resultMessage.value
                gameViewModel.makeMove(x, y)

                if(resultMessage.value != previousResult && resultMessage.value != null){
                    navController.navigate("result")
                }
            }
        )
    }
}
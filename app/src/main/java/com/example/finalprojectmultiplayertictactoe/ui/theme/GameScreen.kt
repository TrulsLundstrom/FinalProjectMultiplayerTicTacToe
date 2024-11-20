package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.*

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController

import com.example.finalprojectmultiplayertictactoe.GameViewModel


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){
    val boardState = gameViewModel.boardState.collectAsState().value
    val resultMessage = gameViewModel.resultMessage.collectAsState().value
    val currentPlayerName = gameViewModel.getCurrentPlayerName()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "$currentPlayerName's turn",
            fontSize = 42.sp,
            modifier = Modifier.padding(top = 128.dp)
        )

        GameBoard(
            boardState = boardState,
            onCellClick = { x, y ->
                gameViewModel.makeMove(x, y)
            }
        )

        resultMessage?.let {
            LaunchedEffect(resultMessage){
                navController.navigate("result")
            }
        }
    }
}
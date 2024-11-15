package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.navigation.NavController

import com.example.finalprojectmultiplayertictactoe.GameViewModel


@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){
    val boardState by gameViewModel.boardState
    val resultMessage by gameViewModel.resultMessage
    val currentPlayerName by remember { derivedStateOf { gameViewModel.getCurrentPlayerName() } }

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
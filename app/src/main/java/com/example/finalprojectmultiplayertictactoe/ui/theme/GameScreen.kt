package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        LaunchedEffect(Unit){
            gameViewModel.resetGame()
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "$currentPlayerName's turn",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
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
}
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
    val boardState = gameViewModel.gameBoard.collectAsState().value
    val resultMessage = gameViewModel.resultMessage.collectAsState().value
    val gameBoardId = gameViewModel.gameBoardDocumentId.collectAsState().value

    val players = gameViewModel.players.collectAsState().value

    val currentPlayerName = when (boardState.currentPlayer){
        "player1" -> boardState.player1
        "player2" -> boardState.player2
        else -> "Unknown Player"
    }

    LaunchedEffect(gameBoardId, players){
        if (gameBoardId == null){
            val player1Name = players.find { it.playerId == "player1" }?.name ?: "Unknown Player 1"
            val player2Name = players.find { it.playerId == "player2" }?.name ?: "Unknown Player 2"

            gameViewModel.createSharedGameBoard(player1Name, player2Name)
        }
        else{
            gameViewModel.loadGameBoard(gameBoardId)
            gameViewModel.listenToGameBoard(gameBoardId)
        }
    }

    LaunchedEffect(boardState){
        gameViewModel.updateGameBoard()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                boardState = boardState.cells,
                onCellClick = { x, y ->
                    val cellKey = "$x$y"
                    gameViewModel.makeMove(cellKey)
                }
            )

            resultMessage?.let { message ->
                LaunchedEffect(resultMessage) {
                    gameViewModel.deleteGameBoard { success ->
                        if(success){ // TA KANSKE BORT DETTA
                            println("Game board deleted successfully.")
                        }
                        else{
                            println("Failed to delete game board.")
                        }
                        navController.navigate("result")
                    }
                }

                Text(
                    text = message,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
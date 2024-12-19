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


/*
gameViewModel är namnet av instansten av GameViewModel
 */

@Composable
fun GameScreen(navController: NavController, gameViewModel: GameViewModel){
    // hämtar nuvarande tillstånd för spelbrädan från instansen gameViewModel
    val boardState = gameViewModel.gameBoard.collectAsState().value

    // hämtar resultat meddelandet när spelet är över
    val resultMessage = gameViewModel.resultMessage.collectAsState().value

    // hämtar spelbrädets dokument ID
    val gameBoardId = gameViewModel.gameBoardDocumentId.collectAsState().value

    // hämtar listan av spelare som är med i spelet (dvs den som skickade och accepterade inbjudandet)
    val players = gameViewModel.players.collectAsState().value

    // avgör vilken spelares tur det är just nu med hänsyn på spelbrädans tillstånd
    val currentPlayerName = when(boardState.currentPlayer){ // currentPlayer tas från DataClasses
        "player1" -> boardState.player1
        "player2" -> boardState.player2
        else -> "Unknown Player"
    }

    // körs när gameBoardId eller players ändras
    LaunchedEffect(gameBoardId, players){
        if(gameBoardId == null){ // om ingen spelbräda finns än, så skapas ett nytt
            val player1Name = players.find { it.playerId == "player1" }?.name ?: "Unknown Player 1"
            val player2Name = players.find { it.playerId == "player2" }?.name ?: "Unknown Player 2"

            gameViewModel.createSharedGameBoard(player1Name, player2Name) // anropar metod som skapar brädan
        }
        else{
            // om ett spelbräde redan finns, ladda det och börja lyssna på förändringar
            gameViewModel.loadGameBoard(gameBoardId)
            gameViewModel.listenToGameBoard(gameBoardId)
        }
    }

    // körs varje gång spelbrädan uppdateras
    LaunchedEffect(boardState){
        gameViewModel.updateGameBoard() // uppdaterar brädan
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

            resultMessage?.let { message -> // om det finns ett resultat meddelande...
                LaunchedEffect(resultMessage){
                    gameViewModel.deleteGameBoard { success ->  // raderar spelbrädan när spelet är klart
                        if(success){
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
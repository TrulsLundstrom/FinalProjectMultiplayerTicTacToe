package com.example.finalprojectmultiplayertictactoe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


// hanterar logik: spelstatus, vems drag det ska bli, brädet och resultatmeddelanden.
class GameViewModel : ViewModel(){

    var player1Name = mutableStateOf<String?>(null)
    val player2Name = "Player 2" // placeholder-namn för spelare 2
    var currentPlayer = mutableStateOf("X")

    var boardState = mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
    var resultMessage = mutableStateOf<String?>(null)

    private val gameLogic = GameLogic()

    fun setPlayer1Name(name: String){
        player1Name.value = name
    }

    fun makeMove(x: Int, y: Int){
        if(boardState.value[x][y] == null && resultMessage.value == null){
            val newBoardState = boardState.value.map { it.copyOf() }.toTypedArray()
            newBoardState[x][y] = currentPlayer.value
            boardState.value = newBoardState

            if(gameLogic.checkWinner(newBoardState, currentPlayer.value)){
                resultMessage.value = if(currentPlayer.value == "X") "${player1Name.value} has won!" else "$player2Name has won!"
            }
            else if(newBoardState.all { row -> row.all { it != null } }){
                resultMessage.value = "It's a draw!"
            }
            else{
                currentPlayer.value = if(currentPlayer.value == "X") "O" else "X"
            }
        }
    }

    fun resetGame(){
        boardState.value = Array(3) { arrayOfNulls(3) }
        currentPlayer.value = "X"
        resultMessage.value = null
    }

}
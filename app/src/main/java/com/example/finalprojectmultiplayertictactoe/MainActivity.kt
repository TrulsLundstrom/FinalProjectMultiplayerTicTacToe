package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.getValue
import androidx.compose.material3.Text

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.finalprojectmultiplayertictactoe.ui.theme.GameBoard
import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen


// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            var player1Name by remember { mutableStateOf<String?>(null) }
            val player2Name by remember { mutableStateOf("Player 2") } // placeholder namn för spelare 2
            var currentPlayer by remember { mutableStateOf("X") } // första symbolen som placeras ut är "X"

            var boardState by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
            val gameLogic = GameLogic() // objekt/instans av den nya klassen GameLogic som hanterar om det är vinnst/förlust/oavgjort
            var resultMessage by remember { mutableStateOf<String?>(null) }

            if(player1Name == null){
                PlayerNameInputScreen { enteredName ->
                    player1Name = enteredName // sparar/lagrar spelarens namn
                }
            }
            else{
                GameBoard(
                    boardState = boardState,
                    onCellClick = { x, y ->

                        if(boardState[x][y] == null && resultMessage == null){ // om rutan är tom, så ska symbolen som representerar den nuvarande spelaren placeras i en ruta
                            boardState = boardState.copyOf().apply { // skapar en ny kopia av boardState med uppdaterad ruta
                                    this[x][y] = currentPlayer
                            }

                            if(gameLogic.checkWinner(boardState, currentPlayer)){
                                resultMessage = if(currentPlayer == "X"){
                                    "$player1Name has won!" // spelare 1 har vunnit
                                }
                                else{
                                    "$player2Name has won!" // spelare 2 har vunnit
                                }
                            }
                            else if(boardState.all { row -> row.all { it != null } }){ // om alla rutor är fyllda så blir det oavgjort
                                resultMessage = "It's a draw!"
                            }
                            else{
                                currentPlayer = if(currentPlayer == "X") "O" else "X" // växlar mellan "X" (spelare 1) och "O" (spelare 2)
                            }
                        }
                    }
                )
            }
            resultMessage?.let {
                Text(text = it)
            }
        }
    }
}



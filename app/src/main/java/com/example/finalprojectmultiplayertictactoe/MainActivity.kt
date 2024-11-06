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
            var playerName by remember { mutableStateOf<String?>(null) }
            var boardState by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
            var currentPlayer by remember { mutableStateOf("X") } // första symbolen som placeras ut är "X"
            val gameLogic = GameLogic() // objekt/instans av den nya klassen GameLogic som hanterar om det är vinnst/förlust/oavgjort
            var winnerMessage by remember { mutableStateOf<String?>(null) }

            if(playerName == null){
                PlayerNameInputScreen { enteredName ->
                    playerName = enteredName // sparar/lagrar spelarens namn
                }
            }
            else{
                GameBoard(
                    boardState = boardState,
                    onCellClick = { x, y ->

                        if(boardState[x][y] == null){ // om rutan är tom, så ska symbolen som representerar den nuvarande spelaren placeras i en ruta
                            boardState = boardState.copyOf().apply{ // skapar en ny kopia av boardState med uppdaterad ruta
                                this[x][y] = currentPlayer
                            }

                            if(gameLogic.checkWinner(boardState, currentPlayer)){
                                winnerMessage = "$playerName has won" // var innan $currentPlayer
                            }
                            else{
                                currentPlayer = if(currentPlayer == "X") "O" else "X" // växlar mellan "X" (spelare 1) och "O" (spelare 2)
                            }
                        }
                    }
                )
            }

            winnerMessage?.let{
                Text(text = it)
            }

        }
    }
}


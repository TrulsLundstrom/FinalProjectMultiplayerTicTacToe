package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.finalprojectmultiplayertictactoe.ui.theme.PlayerNameInputScreen

import com.example.finalprojectmultiplayertictactoe.ui.theme.GameBoard


// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            var playerName by remember { mutableStateOf<String?>(null) }
            val boardState = remember { Array(3) { arrayOfNulls<String>(3) } }

            if(playerName == null){
                PlayerNameInputScreen { enteredName ->
                    playerName = enteredName // sparar/lagrar spelarens namn
                }
            }
            else{
                GameBoard(
                    boardState = boardState,
                    onCellClick = { x, y -> // obs: x = rad, y = kolumn

                        // skriv sen kod här som hanterar drag

                    }
                )
            }
        }
    }
}

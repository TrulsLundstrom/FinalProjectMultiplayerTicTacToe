package com.example.finalprojectmultiplayertictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.finalprojectmultiplayertictactoe.ui.theme.FinalProjectMultiplayerTicTacToeTheme

// hanterar appens livscycel och navigerar
class MainActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent{
            FinalProjectMultiplayerTicTacToeTheme{
            }
        }
    }
}

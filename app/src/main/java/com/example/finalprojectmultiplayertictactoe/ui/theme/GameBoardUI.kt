package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameBoard(boardState: Array<Array<String?>>, onCellClick: (Int, Int) -> Unit){
    Column{
        for(i in 0..2){
            Row{
                for(j in 0..2){
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onCellClick(i, j) },
                        contentAlignment = Alignment.Center
                    ){
                        Text(boardState[i][j] ?: "", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
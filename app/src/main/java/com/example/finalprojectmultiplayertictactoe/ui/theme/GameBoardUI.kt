package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameBoard(boardState: Array<Array<String?>>, onCellClick: (Int, Int) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        for(i in 0..2){
            Row{
                for(j in 0..2){
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.Gray)
                            .clickable { onCellClick(i, j) }
                            .background(if (boardState[i][j] != null) Color.LightGray else Color.White),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            boardState[i][j] ?: "",
                            fontSize = 36.sp,
                            color = when (boardState[i][j]){
                                "X" -> Color.Blue
                                "O" -> Color.Red
                                else -> Color.Black
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
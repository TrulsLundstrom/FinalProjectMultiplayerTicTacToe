
package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
boardState: brädets nuvarande tillstånd
onCellClick:
 */
@Composable
fun GameBoard(boardState: Map<String, String?>, onCellClick: (Int, Int) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        for(i in 0..2){ // loopar igenom raderna 0-2
            Row{
                for(j in 0..2){ // loopar igenom kolumnerna 0-2 i varje rad
                    val cellKey = "$i$j" // skapar en nyckel för cell (ruta). $ används för att sätta samman i och j till en sträng.
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .border(1.dp, Color.Black) // kantlinje runt varje cell
                            .clickable { onCellClick(i, j) },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = boardState[cellKey] ?: "", // OM det finns ett värde för den aktuella cellen (cellkey) i boardState, används det som en text. Om cellen är tom dvs "" så ser cellen tom ut.
                            fontSize = 48.sp
                        )
                    }
                }
            }
        }
    }
}
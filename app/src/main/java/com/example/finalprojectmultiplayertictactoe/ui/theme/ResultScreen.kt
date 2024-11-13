package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController


@Composable
fun ResultScreen(resultMessage: String?, navController: NavController, onReset: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = resultMessage ?: "",
            fontSize = 36.sp,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                onReset()
                navController.navigate("lobby") { popUpTo("game"){
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = "Return to lobby",
                fontSize = 24.sp
            )
        }
    }
}
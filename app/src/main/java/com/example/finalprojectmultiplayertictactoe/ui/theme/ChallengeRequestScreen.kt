package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ChallengeRequestScreen(navController: NavController){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Challenge requests", fontSize = 30.sp)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("lobby") },
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
        ){
            Text(text = "Return to lobby", fontSize = 18.sp)
        }
    }

}




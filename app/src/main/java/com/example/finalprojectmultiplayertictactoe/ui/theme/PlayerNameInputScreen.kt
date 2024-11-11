package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable
fun PlayerNameInputScreen(onNameEntered: (String) -> Unit){

    var playerName by remember { mutableStateOf(TextFieldValue("")) }
    var isNameValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = playerName,
            onValueChange = {
                playerName = it
                isNameValid = it.text.length >= 3
            },
            label = { Text("Enter your name") },
            isError = !isNameValid,
            modifier = Modifier.fillMaxWidth()
        )

        if(!isNameValid){
            Text(
                text = "Name must be at least 3 characters long",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if(playerName.text.isNotEmpty() && isNameValid){
                onNameEntered(playerName.text)
            }
        },
            enabled = isNameValid
        ){
            Text("Continue")
        }
    }
}
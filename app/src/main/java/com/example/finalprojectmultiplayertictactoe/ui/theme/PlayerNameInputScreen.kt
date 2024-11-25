package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.finalprojectmultiplayertictactoe.GameViewModel


@Composable
fun PlayerNameInputScreen(gameViewModel: GameViewModel, onContinue: () -> Unit){
    var playerName by remember { mutableStateOf(TextFieldValue("")) }
    var isNameValid by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Ultimate Tic-tac-toe!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Button(
                onClick = {
                    if(playerName.text.isNotEmpty() && isNameValid){
                        gameViewModel.addPlayerToLobby(playerName.text)
                        onContinue()
                    }
                },
                enabled = isNameValid,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text("Continue", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
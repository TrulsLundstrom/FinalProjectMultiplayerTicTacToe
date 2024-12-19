package com.example.finalprojectmultiplayertictactoe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

    // variabel som håller reda på spelarens namn.
    var playerName by remember { mutableStateOf(TextFieldValue("")) }

    // variabel som indikerar om namnet är giltigt eller inte (ska minst vara 3 tecken långt)
    var isNameValid by remember { mutableStateOf(true) }

    // obs: denna är egentligen onödig med det nuvarande färg temat. Men jag har kvar denna eftersom det underlättar om jag vill ändra färgen
    Box( //
        modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colorScheme.background) // fyller hela skärmen med en bakgrundsfärg (vitt)
    ){
        Column(
            // avstånd på 16 dp från skärmens kanter.
            // Hela column komponenten får ett avstånd från skärmens kanter, inklusive Columnens innehåll.
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            // textfält där spelaren kan skriva in sitt namn
            OutlinedTextField(
                value = playerName, // sätter value till den nuvarande texten i textfältet
                onValueChange = { // körs varje gång spelaren skriver in/ändrar texten i textfält
                    playerName = it // uppdaterar playerName med den nya texten som skrivs in. it representerar den nya texten
                    isNameValid = it.text.length >= 3 // kontrollerar om den nya texten är störe (eller lika med) 3 tecken. blir false eller true
                },
                label = { Text("Enter your name") }, // etikett med texten "Enter your name"
                isError = !isNameValid, // om isError = true så blir outlinen röd
                modifier = Modifier.fillMaxWidth()
            )

            if(!isNameValid){ // om namnet är ogiltigt så visas ett felmeddelande som är rött
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
                    .height(50.dp) //
            ){
                Text("Continue", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
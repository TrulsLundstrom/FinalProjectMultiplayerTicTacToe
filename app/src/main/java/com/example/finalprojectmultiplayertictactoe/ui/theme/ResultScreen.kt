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

/*
argumentet som skickades in till inparametern onReset har inga inparametrar => () , och returnerar inget => Unit

Unit kan jämföras med void i andra programmeringsspråk




 */

@Composable
fun ResultScreen(resultMessage: String?, navController: NavController, onReset: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize(), // fyller hela skärmen
        horizontalAlignment = Alignment.CenterHorizontally, //  centrerar horizontellt (tänk i mitten av "x-axeln")
        verticalArrangement = Arrangement.Center // centrerar vertikalt (tänk i mitten av "y-axeln")
    ){
        Text( // text som visar resultat meddelande
            text = resultMessage ?: "",
            fontSize = 36.sp,
            modifier = Modifier.padding(16.dp)
        )

        Button( // tillbaka till lobby knapp
            onClick = {
                onReset() // återställer spelbrädan,
                navController.navigate("lobby"){ // navigerar till "lobby" skärmen, men sedan tar även bort skärmen "game" och andra skärmar över "game" i back stacken
                    popUpTo("game"){ // popUpTo rensar bort alla skärmar från stacken fram till OCH inklusive "game"
                        inclusive = true
                    }
                }
                // eftersom "lobby" redan finns längst ner i stacken så återanvänds den.
                // Ingen ny "lobby" läggs till.
                // jag gjorde detta eftersom jag tidigare hade problem med navigeringen där jag navigerades till fel skärm
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

/*
förklaring av onRest()

i din MainActivity så ser det ut som att du bara skickar 2st argument till ResultScreen,
men det är faktiskt 3st argument som skickas. Det tredje argumentet är följande:

{
gameViewModel.resetGame()
navController.navigate("lobby")
}

 */


/*
förklaring av pilen "->"

i funktioner: -> beskriver vad en funktion tar in och returnerar.
(inparameter) -> returtyp


lambda uttryck kan kännas igen med att den börjar med { , och slutar med } och har en ->

 */
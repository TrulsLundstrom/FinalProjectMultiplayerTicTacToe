package com.example.finalprojectmultiplayertictactoe

import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

// hanterar logik: spelstatus, vems drag det ska bli, brädet och resultatmeddelanden.
class GameViewModel : ViewModel(){

    var player1Name = mutableStateOf<String?>(null)
    val player2Name = mutableStateOf<String?>(null)
    var currentPlayer = mutableStateOf("X")

    var boardState = mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
    var resultMessage = mutableStateOf<String?>(null)

    private val db = FirebaseFirestore.getInstance()
    private val gameLogic = GameLogic()

    fun setPlayer1Name(name: String){
        player1Name.value = name
        updatePlayerNameInFirestore(name)
    }

    private fun updatePlayerNameInFirestore(name: String){
        val playerData = hashMapOf("name" to name)

        db.collection("players")
            .document("player1")
            .set(playerData)
            .addOnSuccessListener {
                // lyckades uppdatera namnet för första spelaren
                // vet inte vad jag bör ha här. På föreläsningen så användes: if(error != null){ playerList.value = value.toObjects() }
            }
            .addOnFailureListener { e ->
                // misslyckades med att uppdatera namnet för första spelaren
                // vet inte vad jag bör ha här. På föreläsningen så användes: if(error != null){ return @addSnapShotListener }
            }
    }

    fun makeMove(x: Int, y: Int){
        if(boardState.value[x][y] == null && resultMessage.value == null){
            val newBoardState = boardState.value.map { it.copyOf() }.toTypedArray()
            newBoardState[x][y] = currentPlayer.value
            boardState.value = newBoardState

            if(gameLogic.checkWinner(newBoardState, currentPlayer.value)){
                resultMessage.value = if(currentPlayer.value == "X") "${player1Name.value} has won!" else "$player2Name has won!"
            }
            else if(newBoardState.all { row -> row.all { it != null } }){
                resultMessage.value = "It's a draw!"
            }
            else{
                currentPlayer.value = if(currentPlayer.value == "X") "O" else "X"
            }
        }
    }

    fun resetGame(){
        boardState.value = Array(3) { arrayOfNulls(3) }
        currentPlayer.value = "X"
        resultMessage.value = null
    }



    fun fetchPlayers(callback: (List<Player>) -> Unit){
        db.collection("players").get()
            .addOnSuccessListener { result ->
                val players = result.map { document ->
                    Player(
                        playerId = document.id,
                        name = document.getString("name") ?: "",
                        invitation = document.getString("invitation") ?: ""
                    )
                }
                callback(players)
            }
            .addOnFailureListener { e->
                println("Error getting players: $e")
            }
    }

}
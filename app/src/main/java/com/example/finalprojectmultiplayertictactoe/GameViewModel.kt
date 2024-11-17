package com.example.finalprojectmultiplayertictactoe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

// hanterar logik: spelstatus, vems drag det ska bli, brädet och resultatmeddelanden.
class GameViewModel : ViewModel(){
    var players = mutableStateOf<List<Player>>(emptyList())
    var boardState = mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
    var resultMessage = mutableStateOf<String?>(null)

    private val db = FirebaseFirestore.getInstance()
    private val gameLogic = GameLogic()
    private var playersListener: ListenerRegistration? = null
    private var currentPlayerIndex = mutableStateOf(0)
    private val _playerDocumentId = MutableLiveData<String?>(null)

    val playerDocumentId: LiveData<String?> = _playerDocumentId

    fun addPlayerToLobby(playerName: String) {
        db.collection("players")
            .orderBy("playerId", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->

                val nextPlayerId = if(documents.isEmpty){
                    1
                }
                else{
                    val lastPlayerId = documents.first().getString("playerId")
                    lastPlayerId?.removePrefix("player")?.toIntOrNull()?.plus(1) ?: 1
                }

                val newPlayerId = "player$nextPlayerId"

                val playerData = hashMapOf(
                    "name" to playerName,
                    "playerId" to newPlayerId,
                    "invitation" to "notSent"
                )

                db.collection("players")
                    .add(playerData)
                    .addOnSuccessListener { documentReference ->
                        _playerDocumentId.value = documentReference.id
                        println("Player added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding player: $e")
                    }
            }
    }

    fun listenToLobbyPlayers(){
        playersListener = db.collection("players")
            .addSnapshotListener { snapshots, e ->

                if(e != null){
                    println("Listen failed: $e")
                    return@addSnapshotListener
                }

                if(snapshots != null){
                    val updatedPlayers = snapshots.map { document ->
                        Player(
                            playerId = document.id,
                            name = document.getString("name") ?: "",
                            invitation = document.getString("invitation") ?: ""
                        )
                    }
                    players.value = updatedPlayers
                }
            }
    }

    fun deletePlayerFromLobby(documentId: String, onComplete: (Boolean) -> Unit){
        db.collection("players")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                println("Player document deleted")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                println("Error deleting player document: $e")
                onComplete(false)
            }
    }

    fun stopListeningToLobbyPlayers(){
        playersListener?.remove()
    }

    fun makeMove(x: Int, y: Int){
        if(boardState.value[x][y] == null && resultMessage.value == null){
            val newBoardState = boardState.value.map { it.copyOf() }.toTypedArray()
            val currentPlayerSymbol = getCurrentPlayerSymbol()

            newBoardState[x][y] = currentPlayerSymbol
            boardState.value = newBoardState

            if(gameLogic.checkWinner(newBoardState, currentPlayerSymbol)){
                resultMessage.value = "${getCurrentPlayerName()} has won!"

            }
            else if(newBoardState.all { row -> row.all { it != null } }){
                resultMessage.value = "It's a draw!"
            }
            else{
                currentPlayerIndex.value = (currentPlayerIndex.value + 1) % players.value.size
            }
        }
    }

    fun resetGame(){
        boardState.value = Array(3) { arrayOfNulls(3) }
        currentPlayerIndex.value = 0
        resultMessage.value = null
    }

    private fun getCurrentPlayerSymbol(): String{
        return when(currentPlayerIndex.value){
            0 -> "X"
            1 -> "O"
            else -> "P${currentPlayerIndex.value + 1}" // denna är temporär. Just nu när man klickar på "Invite to a challenge" så startas en omgång fast med alla spelarna som finns i lobbyn, för att underlätta så får spelare med en id > 2 en symbol som är P3, P4 etc... (SKA ÄNDRAS SENARE)
        }
    }

    fun getCurrentPlayerName(): String{
        return players.value.getOrNull(currentPlayerIndex.value)?.name ?: "Player ${currentPlayerIndex.value + 1}"
    }

    override fun onCleared(){
        super.onCleared()
        stopListeningToLobbyPlayers()
    }
}
package com.example.finalprojectmultiplayertictactoe

import androidx.lifecycle.ViewModel

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



// hanterar logik: spelstatus, vems drag det ska bli, brädet och resultatmeddelanden.
class GameViewModel : ViewModel(){
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> get() = _players

    private val _boardState = MutableStateFlow(Array(3) { arrayOfNulls<String>(3) })
    val boardState: StateFlow<Array<Array<String?>>> get() = _boardState

    private val _resultMessage = MutableStateFlow<String?>(null)
    val resultMessage: StateFlow<String?> get() = _resultMessage

    private val db = FirebaseFirestore.getInstance()
    private val gameLogic = GameLogic()
    private var playersListener: ListenerRegistration? = null

    private val _currentPlayerIndex = MutableStateFlow(0)

    private val _playerDocumentId = MutableStateFlow<String?>(null)
    val playerDocumentId: StateFlow<String?> get() = _playerDocumentId

    fun addPlayerToLobby(playerName: String){
        db.collection("players")
            .get()
            .addOnSuccessListener { documents ->

                val existingPlayerIds = documents.mapNotNull { document ->
                    document.getString("playerId")?.removePrefix("player")?.toIntOrNull()
                }
                val nextPlayerId = (1..existingPlayerIds.size + 1)
                    .firstOrNull { it !in existingPlayerIds }
                    ?: existingPlayerIds.maxOrNull()?.plus(1) ?: 1

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

                snapshots?.let {
                    val updatedPlayers = it.map { document ->
                        Player(
                            playerId = document.id,
                            name = document.getString("name") ?: "",
                            invitation = document.getString("invitation") ?: ""
                        )
                    }
                    _players.value = updatedPlayers
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

    private fun stopListeningToLobbyPlayers(){
        playersListener?.remove()
    }

    fun makeMove(x: Int, y: Int){
        if(_boardState.value[x][y] == null && _resultMessage.value == null){
            val newBoardState = _boardState.value.map { it.copyOf() }.toTypedArray()
            val currentPlayerSymbol = getCurrentPlayerSymbol()

            newBoardState[x][y] = currentPlayerSymbol
            _boardState.value = newBoardState

            if(gameLogic.checkWinner(newBoardState, currentPlayerSymbol)){
                _resultMessage.value = "${getCurrentPlayerName()} has won!"

            }
            else if(newBoardState.all { row -> row.all { it != null } }){
                _resultMessage.value = "It's a draw!"
            }
            else{
                _currentPlayerIndex.value = (_currentPlayerIndex.value + 1) % _players.value.size
            }
        }
    }

    fun resetGame(){
        _boardState.value = Array(3) { arrayOfNulls(3) }
        _currentPlayerIndex.value = 0
        _resultMessage.value = null
    }

    private fun getCurrentPlayerSymbol(): String{
        return when(_currentPlayerIndex.value){
            0 -> "X"
            1 -> "O"
            else -> throw IllegalStateException("Invalid player index: ${_currentPlayerIndex.value}")
        }
    }

    fun getCurrentPlayerName(): String{
        return _players.value.getOrNull(_currentPlayerIndex.value)?.name ?: "Player ${_currentPlayerIndex.value + 1}"
    }

    override fun onCleared(){
        super.onCleared()
        stopListeningToLobbyPlayers()
    }

    fun startGameWithPlayer(playerId: String){
        val invitedPlayer = _players.value.find { it.playerId == playerId }
        val currentPlayer = _players.value.find { it.playerId == _playerDocumentId.value }

        if(invitedPlayer != null && currentPlayer != null){
            _players.value = listOf(currentPlayer, invitedPlayer)
            _currentPlayerIndex.value = 0
        }
        resetGame()
    }

    fun sendChallenge(challengedPlayerId: String){
        val currentPlayerId = _playerDocumentId.value

        if(currentPlayerId != null){
            val challengeData = hashMapOf(
                "senderId" to currentPlayerId,
                "receiverId" to challengedPlayerId,
                "status" to "pending"
            )

            db.collection("challenges")
                .add(challengeData)
                .addOnSuccessListener { documentReference ->
                    println("Challenge sent with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error sending challenge: $e")
                }
        }
    }
}
package com.example.finalprojectmultiplayertictactoe

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

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

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> get() = _challenges

    private val _challengeRequestCount = MutableStateFlow(0)
    val challengeRequestCount: StateFlow<Int> = _challengeRequestCount

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

    private fun startGameWithPlayer(senderId: String, receiverId: String? = null){
        val invitedPlayer = _players.value.find { it.playerId == senderId }
        val currentPlayer = _players.value.find { it.playerId == _playerDocumentId.value }

        if(invitedPlayer != null && currentPlayer != null){
            _players.value = listOf(currentPlayer, invitedPlayer)
            _currentPlayerIndex.value = 0

            receiverId?.let {
                _playerDocumentId.value = it
            }

            resetGame()
        }
        else{
            println("Error: player not found")
        }
    }

    fun sendChallenge(senderId: String, receiverId: String){
        db.collection("challenges")
            .whereEqualTo("senderId", senderId)
            .whereEqualTo("receiverId", receiverId)
            .get()
            .addOnSuccessListener { snapshots ->
                if(!snapshots.isEmpty){
                    println("A challenge already exists between these players.")
                }
                else{
                    val challenge = hashMapOf(
                        "senderId" to senderId,
                        "receiverId" to receiverId,
                        "status" to "pending",
                    )
                    db.collection("challenges")
                        .add(challenge)
                        .addOnSuccessListener {
                            println("Challenge sent successfully.")
                        }
                        .addOnFailureListener { e ->
                            println("Failed to send challenge: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Failed to check existing challenges: $e")
            }
    }

    fun listenToChallenges(playerId: String){
        db.collection("challenges")
            .whereEqualTo("receiverId", playerId)
            .addSnapshotListener { snapshots, e ->
                if(e != null){
                    println("Listen failed: $e")
                    return@addSnapshotListener
                }

                snapshots?.let {
                    val updatedChallenges = it.documents.mapNotNull { document ->
                        val senderId = document.getString("senderId") ?: return@mapNotNull null
                        val receiverId = document.getString("receiverId") ?: return@mapNotNull null
                        val status = document.getString("status") ?: "pending"
                        Challenge(senderId = senderId, receiverId = receiverId, status = status)
                    }
                    _challenges.value = updatedChallenges
                }
            }
    }

    fun respondToChallenge(challenge: Challenge, accept: Boolean, navController: NavController? = null){
        val newStatus = if(accept) "accepted" else "declined"

        db.collection("challenges")
            .whereEqualTo("senderId", challenge.senderId)
            .whereEqualTo("receiverId", challenge.receiverId)
            .get()
            .addOnSuccessListener { snapshots ->
                for(document in snapshots){
                    if(accept){
                        db.collection("challenges")
                            .document(document.id)
                            .update("status", newStatus)
                            .addOnSuccessListener {
                                println("Challenge accepted")

                                startGameWithPlayer(challenge.senderId, challenge.receiverId)

                                removeChallenge(challenge)

                                navController?.navigate("game")
                            }
                            .addOnFailureListener { e ->
                                println("Failed to update challenge: $e")
                            }
                    }
                    else{
                        db.collection("challenges")
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                println("Challenge declined and document deleted")

                                removeChallenge(challenge)
                            }
                            .addOnFailureListener { e ->
                                println("Failed to delete challenge: $e")
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Failed to fetch challenges: $e")
            }
    }


    fun listenToChallengeRequests(){
        playerDocumentId.value?.let { playerId ->
            db.collection("challenges")
                .whereEqualTo("receiverId", playerId)
                .addSnapshotListener { snapshot, exception ->
                    if(exception != null){
                        println("Listen failed: $exception")
                        return@addSnapshotListener
                    }

                    if(snapshot != null){
                        _challengeRequestCount.value = snapshot.size()
                    }
                }
        }
    }

    private fun removeChallenge(challenge: Challenge) {
        _challenges.value = _challenges.value.filterNot {
            it.senderId == challenge.senderId && it.receiverId == challenge.receiverId
        }

        if(_challengeRequestCount.value > 0){
            _challengeRequestCount.value -= 1
        }
    }

}

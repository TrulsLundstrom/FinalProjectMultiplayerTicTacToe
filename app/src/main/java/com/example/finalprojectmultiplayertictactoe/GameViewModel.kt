package com.example.finalprojectmultiplayertictactoe

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class GameBoard(
    val cells: Map<String, String?> = mapOf(
        "00" to null, "01" to null, "02" to null,
        "10" to null, "11" to null, "12" to null,
        "20" to null, "21" to null, "22" to null
    ),
    val player1: String = "",
    val player2: String = "",
    val currentPlayer: String = ""
)

class GameViewModel : ViewModel(){
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> get() = _players

    private val _boardState = MutableStateFlow(Array(3) { arrayOfNulls<String>(3) })

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

    private val _gameBoard = MutableStateFlow(GameBoard())
    val gameBoard: StateFlow<GameBoard> get() = _gameBoard

    private val _gameBoardDocumentId = MutableStateFlow<String?>(null)
    val gameBoardDocumentId: StateFlow<String?> get() = _gameBoardDocumentId

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

    fun makeMove(cellKey: String){
        if(_gameBoard.value.cells[cellKey] == null){
            val currentPlayerSymbol = getCurrentPlayerSymbol()
            val updatedCells = _gameBoard.value.cells.toMutableMap()
            updatedCells[cellKey] = currentPlayerSymbol

            if(gameLogic.checkWinner(updatedCells, currentPlayerSymbol)){
                _resultMessage.value = "${getCurrentPlayerName()} wins!"
                return
            }

            if(updatedCells.values.all { it != null }){
                _resultMessage.value = "It's a draw!"
                return
            }
            _gameBoard.value = _gameBoard.value.copy(
                cells = updatedCells,
                currentPlayer = if(_gameBoard.value.currentPlayer == "player1") "player2" else "player1"
            )

            _gameBoardDocumentId.value?.let { documentId ->
                db.collection("game_boards")
                    .document(documentId)
                    .update("cells", updatedCells, "currentPlayer", _gameBoard.value.currentPlayer)
                    .addOnSuccessListener {
                        println("Move updated successfully.")
                    }
                    .addOnFailureListener { e ->
                        println("Error updating move: $e")
                    }
            }
        }
    }

    fun resetGame(){
        _boardState.value = Array(3) { arrayOfNulls(3) }
        _currentPlayerIndex.value = 0
        _resultMessage.value = null
    }

    private fun getCurrentPlayerSymbol(): String{
        return when (_gameBoard.value.currentPlayer) {
            "player1" -> "X"
            "player2" -> "O"
            else -> throw IllegalStateException("Invalid current player")
        }
    }

    private fun getCurrentPlayerName(): String{
        return when (_gameBoard.value.currentPlayer) {
            "player1" -> _gameBoard.value.player1
            "player2" -> _gameBoard.value.player2
            else -> "Unknown Player"
        }
    }

    override fun onCleared(){
        super.onCleared()
        stopListeningToLobbyPlayers()
    }

    fun sendChallenge(senderId: String, receiverId: String, navController: NavController?){
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

                            val senderName = players.value.find { it.playerId == senderId }?.name ?: "Unknown Player"
                            val receiverName = players.value.find { it.playerId == receiverId }?.name ?: "Unknown Player"

                            createSharedGameBoard(senderName, receiverName)
                            navController?.navigate("game")
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
                    val challengeDocRef = db.collection("challenges").document(document.id)

                    if(accept){
                        challengeDocRef.update("status", newStatus)
                            .addOnSuccessListener {
                                println("Challenge accepted")

                                db.collection("game_boards")
                                    .whereEqualTo("player1", players.value.find { it.playerId == challenge.senderId }?.name) // Query by player names
                                    .whereEqualTo("player2", players.value.find { it.playerId == challenge.receiverId }?.name)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        if(!documents.isEmpty){
                                            val gameBoardDoc = documents.documents[0]
                                            _gameBoardDocumentId.value = gameBoardDoc.id
                                            loadGameBoard(gameBoardDoc.id) // Load the existing game board data
                                            navController?.navigate("game")
                                        }
                                        else{
                                            println("No game board found. This should not happen if a game was created.")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error getting game board document: $e")
                                    }

                                challengeDocRef.delete()
                                    .addOnSuccessListener {
                                        println("Challenge document deleted after acceptance")
                                        removeChallenge(challenge)
                                    }
                                    .addOnFailureListener { e ->
                                        println("Failed to delete challenge after acceptance: $e")
                                    }
                            }
                            .addOnFailureListener { e ->
                                println("Failed to update challenge status: $e")
                            }
                    }
                    else{
                        challengeDocRef.delete()
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

    private fun removeChallenge(challenge: Challenge){
        _challenges.value = _challenges.value.filterNot {
            it.senderId == challenge.senderId && it.receiverId == challenge.receiverId
        }

        if(_challengeRequestCount.value > 0){
            _challengeRequestCount.value -= 1
        }
    }

    fun createSharedGameBoard(player1Name: String, player2Name: String){
        db.collection("game_boards")
            .whereEqualTo("player1", player1Name)
            .whereEqualTo("player2", player2Name)
            .get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    val newGameBoard = GameBoard(
                        player1 = player1Name,
                        player2 = player2Name,
                        currentPlayer = "player1"
                    )
                    db.collection("game_boards")
                        .add(newGameBoard)
                        .addOnSuccessListener { documentReference ->
                            _gameBoardDocumentId.value = documentReference.id
                            println("Shared game board created with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Error creating shared game board: $e")
                        }
                }
                else{
                    val gameBoardDoc = documents.documents[0]
                    _gameBoardDocumentId.value = gameBoardDoc.id
                    println("Using existing shared game board with ID: ${gameBoardDoc.id}")
                    loadGameBoard(gameBoardDoc.id)
                }
            }
            .addOnFailureListener { e ->
                println("Error checking for existing game board: $e")
            }
    }

    fun loadGameBoard(documentId: String){
        db.collection("game_boards")
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    val gameBoard = document.toObject(GameBoard::class.java)
                    _gameBoard.value = gameBoard ?: GameBoard()
                }
            }
            .addOnFailureListener { e ->
                println("Error loading game board: $e")
            }
    }

    fun updateGameBoard(){
        _gameBoardDocumentId.value?.let { documentId ->
            val updatedGameBoard = _gameBoard.value

            db.collection("game_boards")
                .document(documentId)
                .set(updatedGameBoard)
                .addOnSuccessListener {
                    println("Game board updated successfully.")
                }
                .addOnFailureListener { e ->
                    println("Error updating game board: $e")
                }
        }
    }

    fun deleteGameBoard(onComplete: (Boolean) -> Unit){
        _gameBoardDocumentId.value?.let { documentId ->
            db.collection("game_boards")
                .document(documentId)
                .delete()
                .addOnSuccessListener {
                    println("Game board document deleted successfully.")
                    _gameBoardDocumentId.value = null
                    onComplete(true)
                }
                .addOnFailureListener { e ->
                    println("Error deleting game board: $e")
                    onComplete(false)
                }
        } ?: run {
            println("No game board document ID found.")
            onComplete(false)
        }
    }

    fun listenToGameBoard(documentId: String){
        db.collection("game_boards")
            .document(documentId)
            .addSnapshotListener { snapshot, e ->
                if(e != null){
                    println("Listen failed: $e")
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val updatedGameBoard = it.toObject(GameBoard::class.java)
                    if(updatedGameBoard != null){
                        _gameBoard.value = updatedGameBoard
                    }
                }
            }
    }

}
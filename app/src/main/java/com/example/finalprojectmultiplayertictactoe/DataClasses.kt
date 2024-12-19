package com.example.finalprojectmultiplayertictactoe

// representerar utmaningar
data class Challenge(
    val senderId: String = "", // dokument ID för den som skickar utmaningen
    val receiverId: String = "", // dokument ID för den som får utmaningen
    val status: String = "pending"
)

// representerar spelare
data class Player(
    val playerId: String = "", // t.ex "player1" , "player2" osv
    var name: String = "",  // namnet som spelaren skrev in
)

// representerar själva spelbrädet
data class GameBoard(
    val cells: Map<String, String?> = mapOf( // mapOf => cells blir immutable
        "00" to null, "01" to null, "02" to null,
        "10" to null, "11" to null, "12" to null,
        "20" to null, "21" to null, "22" to null
    ),
    val player1: String = "",  // lagrar spelarnas namn
    val player2: String = "",  // lagrar spelarnas namn
    val currentPlayer: String = "" // lagrar nuvarande spelares namn
)

/*
    ________________
    | 00 | 01 | 02 |
    ----------------
    | 10 | 11 | 12 |
    ----------------
    | 20 | 21 | 22 |
    ----------------
 */
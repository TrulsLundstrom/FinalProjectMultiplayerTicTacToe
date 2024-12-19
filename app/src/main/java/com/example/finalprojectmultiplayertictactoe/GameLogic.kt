package com.example.finalprojectmultiplayertictactoe


// GameLogic klassen hanterar själva spel logiken, dvs kontrollerar om någon har vunnit
class GameLogic{


    /*
    board: en map som representerar spelbrädet
    player: namnet på spelaren som man kontrollerar vinnst på
     */
    fun checkWinner(board: Map<String, String?>, player: String): Boolean{
        val winningCombinations = listOf(
            // horisontella rader
            listOf("00", "01", "02"), // rad 1
            listOf("10", "11", "12"), // rad 2
            listOf("20", "21", "22"), // rad 3

            // vertikala kolumner
            listOf("00", "10", "20"), // kolumn 1
            listOf("01", "11", "21"), // kolumn 2
            listOf("02", "12", "22"), // kolumn 3

            // diagonaler
            listOf("00", "11", "22"), // från vänster till höger
            listOf("02", "11", "20")  // från höger till vänster
        )

        /*
        här kontrolleras om någon av de vinnande kombinationerna är helt fyllda av den angivna spelaren
        comination.all: för varje vinnande kombination, kontrolleras att varje position i kombinationen är fylld av den angivna spelaren, dvs ALLA möjliga kombinationer testas
         */
        return winningCombinations.any { combination -> combination.all { position -> board[position] == player } }
    }
}
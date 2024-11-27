package com.example.finalprojectmultiplayertictactoe

class GameLogic{

    fun checkWinner(board: Map<String, String?>, player: String): Boolean{
        val winningCombinations = listOf(
            // horisontella rader
            listOf("00", "01", "02"),
            listOf("10", "11", "12"),
            listOf("20", "21", "22"),

            // vertikala kolumner
            listOf("00", "10", "20"),
            listOf("01", "11", "21"),
            listOf("02", "12", "22"),

            // diagonaler
            listOf("00", "11", "22"),
            listOf("02", "11", "20")
        )

        return winningCombinations.any { combination ->
            combination.all { position -> board[position] == player }
        }
    }
}
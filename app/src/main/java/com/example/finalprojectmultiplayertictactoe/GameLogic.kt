package com.example.finalprojectmultiplayertictactoe

class GameLogic{

    fun checkWinner(board: Array<Array<String?>>, player: String): Boolean{
        val winningCombinations = listOf(
            listOf(Pair(0, 0), Pair(0, 1), Pair(0,2)),
            listOf(Pair(1, 0), Pair(1, 1), Pair(1,2)),
            listOf(Pair(2, 0), Pair(2, 1), Pair(2,2)),

            listOf(Pair(0, 0), Pair(1, 0), Pair(2,0)),
            listOf(Pair(0, 1), Pair(1, 1), Pair(2,1)),
            listOf(Pair(0, 2), Pair(1, 2), Pair(2,2)),

            listOf(Pair(0, 0), Pair(1, 1), Pair(2,2)),
            listOf(Pair(0, 2), Pair(1, 1), Pair(2,0))

        )

        return winningCombinations.any{ combination ->
            combination.all { (x, y) -> board[x][y] == player }
        }
    }
}
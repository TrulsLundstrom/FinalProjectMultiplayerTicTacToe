package com.example.finalprojectmultiplayertictactoe

// hanterar logik: spelstatus, vems drag, etc... (och kontrollerar giltighet av drag osv)
class GameViewModel{

    /*
    skriv de variabler som behövs är (om det ens behövs)

    */

    // metod för att starta ett nytt spel
    fun startNewGame(player1: String, player2: String){
        // skriv logiken här för att starta ett nytt spel
    }


    // gör draget senare vid GameBoard
    fun makeMove(x: Int, y: Int): Boolean{
        // skriv logiken för att utföra drag på brädan och uppdatera sammtidigt tillståndet

        return // SKRIV NÅGOT HÄR
    }

    fun resetGame(){
        gameBoard.resetBoard()
    }


}
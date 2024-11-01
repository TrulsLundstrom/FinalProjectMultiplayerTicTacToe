package com.example.finalprojectmultiplayertictactoe

// hanterar logik: spelstatus, vems drag, etc... (och kontrollerar giltighet av drag osv)
class GameViewModel{

    // metod för att starta ett nytt spel
    fun startNewGame(player1: String, player2: String){

    }

    // gör draget senare vid GameBoard. Det är inte GameViewModel som utför själva draget, den anropar istället GameBoard
    // makeMove hanterar draget och uppdaterar tillstånd
    // obs: denna har ett annat antal inparametrar än den i GameBoard klassen
    // genom polymorphism så kan man anropa metoder/funktioner med samma namn, eftersom de har olika inparametrar.
    fun makeMove(x: Int, y: Int): Boolean{

        return // SKRIV NÅGOT HÄR
    }

    fun resetGame(){
        gameBoard.resetBoard()
    }


}
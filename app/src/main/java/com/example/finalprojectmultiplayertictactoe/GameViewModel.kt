package com.example.finalprojectmultiplayertictactoe

// hanterar logik: spelstatus, vems drag, etc... (och kontrollerar giltighet av drag osv)
class GameViewModel{

    // denna metoden ska indikera att en spelare är redo och skicka ett meddelande till motståndaren
    fun playerReady(){

    }

    fun opponentReady(){

    }

    // metod som kontrollerar om båda spelarna är redo, och isåfall startar spelet
    private fun checkIfBothPlayersReady(){

    }

    // startar spelet
    private fun startGame(){

    }

    // metod för att starta ett NYTT spel
    fun startNewGame(player1: String, player2: String){

    }

    // gör draget via GameBoard. Det är inte GameViewModel som utför själva draget, den anropar istället GameBoard
    // makeMove (i denna klassen) hanterar draget och uppdaterar tillstånd
    fun makeMove(x: Int, y: Int): Boolean{

        return true // ÄNDRA (KANSKE)
    }

    // på liknande sätt som metoden ovanför, så är det INTE klassen GameViewModel som utför själva återställningen av brädet-
    // , GameViewModel anropar istället metoden resetGame i GameBoard.
    fun resetGame(){

    }

}
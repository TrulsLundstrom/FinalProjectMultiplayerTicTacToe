package com.example.finalprojectmultiplayertictactoe

// ger resultatet (dvs vinnst, förlust eller oavgjort) och ger alternativet att gå tillbaka till lobbyn
class ResultScreen{

    // metod för att visa resultatet
    fun showResult(winner: String?, isDraw: Boolean){
        // skriv här logiken för att visa resultatet, t.ex uppdatera UI:n
    }

    // metod för gå tillbaka till lobbyn
    fun returnToLobby(){
        // skriv här logiken för att gå tillbaka till lobbyn
    }

    // metod för att ge ett meddelande till vinnaren
    fun showWinnerMessage(winner: String?){
        // skriv här logiken för att visa ett meddelande till vinnaren
    }

    // metod för att ge ett meddelande till förloraren
    fun showLoserMessage(loser: String?){
        // skriv här logiken för att visa ett meddelande till förloraren
    }

    // metod för att ge ett meddelande om det är oavgjort
    fun showDrawMessage(){
        // skriv här logiken för att visa ett meddelande om det är oavgjort
    }

}
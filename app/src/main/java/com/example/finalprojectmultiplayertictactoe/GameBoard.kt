package com.example.finalprojectmultiplayertictactoe

// representerar själva brädet och hanterar placeringen av symboler (dvs X och O) samt kontrollerar om en vinnst skedde
class GameBoard{
    private val board: Array<Array<String?>> = Array(3) { arrayOfNulls<String?>(3) } // 2D array

    // logik för att placera en spelares symbol (OM en ruta ör TOM)
    fun placeMove(x: Int, y: Int, playerSymbol: String): Boolean{

        return // SKRIV NÅGOT HÄR
    }

    // kontrollerar om någon har vunnit (man behöver inte kolla om en spelare har förlorat eftersom om en spelare vinner , så vet man ju redan att den andra förlorade)
    fun checkWin(playerSymbol: String): Boolean{

        return // SKRIV NÅGOT HÄR
    }

    // kontrollerar om det är oavgjort
    fun checkDraw(): Boolean{

        return // SKRIV NÅGOT HÄR
    }

    // en getter-funktion, som returnerar brädets tillstånd. Någon anropar denna funktionen och får ("get") brädets tillstånd.
    fun getBoardState(): Array<Array<String?>>{

        return board;
    }

}

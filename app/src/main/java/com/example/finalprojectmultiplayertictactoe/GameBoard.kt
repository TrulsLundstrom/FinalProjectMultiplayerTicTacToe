package com.example.finalprojectmultiplayertictactoe

// representerar själva brädet och hanterar placeringen av symboler (dvs X och O) samt kontrollerar om en vinnst skedde
class GameBoard{
    private val board: Array<Array<String?>> = Array(3) { arrayOfNulls<String?>(3) } // 2D array som lagrar raderna och kolumner

    // metod som kontrollerar om vinnst har hänt
    fun checkWin(): Boolean{

        return
    }

    // metod som kontrollerar om det är oavgjort
    fun checkDraw(): Boolean{

        return
    }

    // metod som utför ett drag
    fun makeMove(player: String, x: Int, y: Int): Boolean{

        return
    }

    // metod som återställer brädet
    fun resetBoard(){

    }

}

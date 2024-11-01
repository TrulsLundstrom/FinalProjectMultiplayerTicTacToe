package com.example.finalprojectmultiplayertictactoe

// hanterar serveranslutning och kommunikation, men även möjligheten för spelare att skicka/ta emot meddelanden
class NetworkManager{

    // metod för att ansluta till en server
    fun connectToServer(): Boolean{ // jag är osäker om Boolean ska returneras. Men jag har det såhär just nu iallafall

        return
    }

    // skickar ett meddelande att en spelare är redo
    fun sendReadyMessage(){

    }

    // tar emot meddelandet som indikerade att motståndaren är redo
    fun reciveReadyMessage(): Boolean{

    }

    // metod för att skicka meddelanden
    fun sendMessage(message: String){

    }

    // metod för att ta emot meddelanden från andra spelare
    fun reciveMessage(): String{

        return
    }

    // metod för att koppla bort från servern
    fun disconnectFromServer(){ // kanske inte ens behövs. När man stänger av appen så kopplas man kasnke bort automatiskt från servern?

    }
}
package com.almitasoft.choremeapp.model

abstract class Chore(var givenBy: Chore,
                     var givenTo: Chore) {
}

class TextChore(var choreMessage: String,
                givenBy: Chore,
                givenTo: Chore) : Chore(givenBy,givenTo){

}

class RecorededChore(givenBy: Chore,
                     givenTo: Chore): Chore(givenBy,givenTo){

}

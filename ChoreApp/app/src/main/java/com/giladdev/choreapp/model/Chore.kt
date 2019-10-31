package com.giladdev.choreapp.model

import java.text.DateFormat
import java.util.*

class Chore() {
    var choreName: String?= null
    var assignedBy: String?=null
    var assignedTo: String?=null
    var timerAssigned:Long=0
    var id: Int?= null

    constructor(choreName : String, assignedBy : String,
                assignedTo : String, timeAssigned:Long,
                id : Int) : this(){
        this.choreName = choreName
        this.assignedBy = assignedBy
        this.assignedTo = assignedTo
        this.timerAssigned = timeAssigned
        this.id = id
    }

    fun GetTimeAsString() : String{
        var dateFormat = DateFormat.getDateInstance()
        var formattedDate: String = dateFormat.format(Date(timerAssigned).time)

        return formattedDate
    }

}

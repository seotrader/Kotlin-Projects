package com.almitasoft.choremeapp.Utils

import java.text.DateFormat
import java.util.*

fun GetTimeAsString(time : Long) : String{
    var dateFormat = DateFormat.getDateInstance()
    var formattedDate: String = dateFormat.format(Date(time).time)

    return formattedDate
}
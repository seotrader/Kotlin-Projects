package com.almitasoft.choremeapp.model

data class Task (var taskName : String,
                 var assignedUser : String,
                 var assignedUserID : String,
                 var assignedTo : String,
                 var assignedToUserID: String,
                 var expirationDate : Long)
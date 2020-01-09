package com.almitasoft.choremeapp.model

abstract class Notification (var notificationType : Int,
                             var notificationMessage: String) {
}

class AddFriendNotification( notificationMessage: String) : Notification(0,notificationMessage)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
    var status: String=""
}

class AddTaskNotification( notificationMessage: String) : Notification(1,notificationMessage)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
}




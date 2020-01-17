package com.almitasoft.choremeapp.model

enum class NotificationType{
    ADDFRIEND,
    ADDTASK,
    GENERALNOTIFICATION
}
abstract class Notification (var notificationType : NotificationType,
                             var notificationMessage: String,
                             var notificationID: String) {


}

class AddFriendNotification( notificationMessage: String, notificationID: String)
    : Notification(NotificationType.ADDFRIEND,notificationMessage,notificationID)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
    var status: String=""
}

class AddTaskNotification( notificationMessage: String,notificationID: String) :
    Notification(NotificationType.ADDTASK,notificationMessage,notificationID)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
}




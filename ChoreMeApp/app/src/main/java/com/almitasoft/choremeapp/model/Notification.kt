package com.almitasoft.choremeapp.model

enum class NotificationType{
    ADDFRIEND,
    ADDTASK,
    NEWFRIENDNOTIFICATION,
    GENERALNOTIFICATION
}
abstract class Notification (var notificationType : NotificationType,
                             var notificationMessage: String,
                             var notificationID: String) {


}

class GeneralNotification(notificationMessage: String, notificationID: String,type : NotificationType) :
        Notification(type,notificationMessage,notificationID){
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
    var notificationThumbImgSource=""
    var notificationThumbImgDestination=""
}


class NewFriendNotification( notificationMessage: String, notificationID: String)
    : Notification(NotificationType.NEWFRIENDNOTIFICATION,notificationMessage,notificationID){
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
    var status: String=""

}

class AddFriendNotification( notificationMessage: String, notificationID: String)
    : Notification(NotificationType.ADDFRIEND,notificationMessage,notificationID)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
    var status: String=""
    var notificationThumbImgSource=""
    var notificationThumbImgDestination=""
}

class AddTaskNotification( notificationMessage: String,notificationID: String) :
    Notification(NotificationType.ADDTASK,notificationMessage,notificationID)
{
    var sourceUID : String=""
    var sourceUName : String=""
    var targetUID: String=""
    var targetUName: String=""
}




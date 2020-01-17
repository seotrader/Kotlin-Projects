package com.almitasoft.choremeapp.model

// Singelton object
object CurrentUser
{
        var displanyName : String?=null
        var userEmail: String?= null
        var userID : String?= null
        var status: String?=null
        var thumb_image_url : String?= null
        var image_url : String?= null
        var friendsList : ArrayList<User>?=null
}
package com.almitasoft.choremeapp.model

data class User(var displayName : String,
                var userID : String) {
    var userEmail: String?= null
    var status: String?=null
    var thumb_image_url : String?= null
    var image_url : String?= null
}
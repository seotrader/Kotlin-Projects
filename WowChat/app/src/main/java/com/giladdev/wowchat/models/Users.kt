package com.giladdev.wowchat.models

class Users()  {
    var display_name: String?=null
    var image: String?=null
    var thumb_image: String?= null
    var user_status: String?=null

    constructor(display_name: String,
                image: String,
                thumb_imagee : String,
                user_status: String) : this(){
        this.display_name = display_name
        this.image = image
        this.thumb_image =  thumb_imagee
        this.user_status = user_status
    }
}
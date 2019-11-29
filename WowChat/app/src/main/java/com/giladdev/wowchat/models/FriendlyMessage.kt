package com.giladdev.wowchat.models

class FriendlyMessage() {
    var id: String? = null
    var text:String?= null
    var from: String?= null
    var to: String?=null

    constructor(id : String,
                text: String,
                from: String,
                to: String): this() {
        this.id = id
        this.text = text
        this.from = from
        this.to = to
    }
}
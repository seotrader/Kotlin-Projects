package com.almitasoft.wowchat.models

class FriendlyMessage() {
    var id: String? = null
    var dest_id: String?= null
    var text:String?= null
    var from: String?= null
    var to: String?=null

    constructor(
        id: String,
        dest_id: String,
        text: String,
        from: String,
        to: String
    ): this() {
        this.id = id
        this.dest_id = dest_id
        this.text = text
        this.from = from
        this.to = to
    }
}
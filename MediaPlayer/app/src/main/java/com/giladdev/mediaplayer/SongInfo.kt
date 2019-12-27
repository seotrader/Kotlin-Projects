package com.giladdev.mediaplayer

class SongInfo {
    var title:String? = null
    var authorName:String? = null
    var songURL: String?= null
    constructor(title: String, authorName:String, songUrl: String){
        this.title = title
        this.authorName = authorName
        this.songURL= songUrl
    }
}
package com.almitasoft.choremeapp.model

sealed class Result(var result: String)

data class CreateAccountFullResult(var res : String,
                                   var displayName : String,
                                   var status : String,
                                   var imageUrl: String,
                                   var thumb_image_url : String) : Result(res)

data class CreateAccountResult(var res : String) : Result(res)

data class LoginResult(var res: String,
                       var displayName: String) : Result(res)


data class AddFriendResult(var res: String) : Result(res)

data class GetUserDataResult(var res : String) : Result(res)


//abstract class Result (var result: String){
//}
//
//class CreateAccountResult(result: String) : Result(result) {
//    var displayName : String?=null
//    var status : String?=null
//    var imageUrl: String?=null
//    var thumb_image_url : String?=null
//}
package com.godspeed.chatrio.model

class User {
    var uid: String? = null
    var name: String? = null
    var number: String? = null
    var image: String? = null
    constructor(){}
    constructor(
        uid:String?,
        name: String?,
        number: String?,
        image: String?
    ){
        this.uid = uid
        this.name = name
        this.number = number
        this.image = image
    }
}
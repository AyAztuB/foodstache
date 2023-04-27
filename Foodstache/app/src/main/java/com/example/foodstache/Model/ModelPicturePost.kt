package com.example.foodstache.Model

class ModelPicturePost {
    var postID : String = ""
    var description : String = ""
    var userID : String = ""
    var Image : String = ""
    var Time : String = ""
    var Username : String = ""
    var userPP : String = ""

    constructor()

    constructor(postID : String, description : String, userID : String, Image : String, Time : String, Username : String, userPP : String) {
        this.postID = postID
        this.description = description
        this.userPP = userPP
        this.userID = userID
        this.Image = Image
        this.Time = Time
        this.Username = Username
    }

}
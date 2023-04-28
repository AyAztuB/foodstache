package com.example.foodstache.Model

class ModelPostInProfile {
    var userID : String = ""
    var Time : String = ""
    var Username : String = ""
    var userPP : String = ""

    var description : String = ""
    var picture : String = ""
    var video : String = ""
    var title : String = ""
    var Ingredients : ArrayList<String>? = null
    var Steps : ArrayList<String>? = null

    constructor()

    constructor(userID : String, Time : String, Username : String, userPP : String,
                description : String, picture : String, video : String, title : String,
                Ingredients : ArrayList<String>?, Steps : ArrayList<String>?) {
        this.userID = userID
        this.title = title
        this.userPP = userPP
        this.Time = Time
        this.Username = Username
        this.Ingredients = Ingredients
        this.Steps = Steps
        this.picture = picture
        this.video = video
        this.description = description
    }

}
package com.example.foodstache.Model

class ModelRecipePost {
    var userID : String = ""
    var title : String = ""
    var Time : String = ""
    var Username : String = ""
    var userPP : String = ""
    var Ingredients : ArrayList<String> = ArrayList()
    var Steps : ArrayList<String> = ArrayList()

    constructor()

    constructor(userID : String, title : String, Time : String, Username : String, userPP : String,
                Ingredients : ArrayList<String>, Steps : ArrayList<String>) {
        this.userID = userID
        this.title = title
        this.userPP = userPP
        this.Time = Time
        this.Username = Username
        this.Ingredients = Ingredients
        this.Steps = Steps
    }
}
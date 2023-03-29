package com.example.foodstache.Model

class User {

    private var username : String = ""
    private var fullName : String = ""
    private var bio : String = ""
    private var nbFollowing : String = ""
    private var nbFollowers : String = ""
    private var nbPost : String = ""
    private var pp : String = ""
    private var userId : String = ""

    constructor()

    constructor(username : String, bio : String, nbFollowing : String, nbFollowers : String, nbPost : String, pp : String, userId: String, fullName : String ){
        this.username = username
        this.bio = bio
        this.nbFollowing = nbFollowing
        this.nbFollowers = nbFollowers
        this.nbPost = nbPost
        this.pp = pp
        this.userId = userId
        this.fullName = fullName
    }

    fun getUsername() : String{
        return username
    }

    fun setUsername(username: String){
        this.username= username
    }
    fun getBio() : String{
        return bio
    }

    fun setBio(bio: String){
        this.bio= bio
    }

    fun getNbFollowing() : String{
        return nbFollowing
    }

    fun setNbFollowing(nbFollowing: String){
        this.nbFollowing= nbFollowing
    }

    fun getNbFollowers() : String{
        return nbFollowers
    }

    fun setNbFollowers(nbFollowers: String){
        this.nbFollowers= nbFollowers
    }

    fun getNbPost() : String{
        return nbPost
    }

    fun setNbPost(nbPost: String){
        this.nbPost= nbPost
    }

    fun getPp() : String{
        return pp
    }

    fun setPp(pp: String){
        this.pp= pp
    }

    fun getUserId() : String{
        return userId
    }

    fun setUserId(userId: String){
        this.userId= userId
    }

    fun getFullName(): String {
        return fullName
    }

    fun setFullName(fullName : String){
        this.fullName = fullName
    }


}
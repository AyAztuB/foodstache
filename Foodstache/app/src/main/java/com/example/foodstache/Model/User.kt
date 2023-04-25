package com.example.foodstache.Model

class User {

    private var username : String = ""
    private var name : String = ""
    private var bio : String = ""
    private var nbFollowing : String = ""
    private var nbFollowers : String = ""
    private var nbPost : String = ""
    private var image : String = ""
    private var uid : String = ""

    constructor()

    constructor(username : String, bio : String, nbFollowing : String, nbFollowers : String, nbPost : String, image : String, uid: String, name : String ){
        this.username = username
        this.bio = bio
        this.nbFollowing = nbFollowing
        this.nbFollowers = nbFollowers
        this.nbPost = nbPost
        this.image = image
        this.uid = uid
        this.name = name
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

    fun getImage() : String{
        return image
    }

    fun setImage(image: String){
        this.image= image
    }

    fun getUid() : String{
        return uid
    }

    fun setUid(uid: String){
        this.uid= uid
    }

    fun getName(): String {
        return name
    }

    fun setName(name : String){
        this.name = name
    }


}
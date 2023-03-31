package com.example.foodstache

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.foodstache.Fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var firebaseDatabase : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference

    // views
    private lateinit var settingsBtn : ImageButton
    private lateinit var backBtn : ImageButton
    private lateinit var mUsernameTv : TextView
    private lateinit var mNameTv : TextView
    private lateinit var mBioTv : TextView
    private lateinit var nbPostTv : TextView
    private lateinit var nbFollowersTv : TextView
    private lateinit var nbFollowingTv : TextView
    private lateinit var mAvatarView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        /*
        // ActionBar and its title
        val actionBar : ActionBar = supportActionBar!!
        actionBar.title = "Profile"
        */

        // init
        firebaseAuth = FirebaseAuth.getInstance()

        // init views
        settingsBtn = findViewById(R.id.options_btn)
        backBtn = findViewById(R.id.back_btn)
        mUsernameTv = findViewById(R.id.profile_activity)
        mNameTv = findViewById(R.id.full_name)
        mBioTv = findViewById(R.id.bio)
        nbPostTv = findViewById(R.id.total_posts)
        nbFollowersTv = findViewById(R.id.total_followers)
        nbFollowingTv = findViewById(R.id.total_following)
        mAvatarView = findViewById(R.id.profile_image)

        settingsBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@ProfileActivity, SettingActivity::class.java))
        }

        backBtn.setOnClickListener {
            // start LoginActivity
            startActivity(Intent(this@ProfileActivity, BottomNavigation::class.java))
        }
    }

    private fun checkUserStatus() {
        // get current user
        val _user = firebaseAuth.currentUser
        if(_user == null) {
            // user not signed in, go to MainActivity
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finish()
        }
        else {
            // user is signed in, stay here
            // set user info
            user = _user
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase.getReference("Users")
            val query : Query = databaseReference.orderByChild("uid").equalTo(user.uid)
            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        // get user info from database
                        val name = ""+ds.child("name").value
                        val username = ""+ds.child("username").value
                        val nbPost = ""+ds.child("nbPost").value
                        val nbFollowers = ""+ds.child("nbFollowers").value
                        val nbFollowing = ""+ds.child("nbFollowing").value
                        val image = ""+ds.child("image").value
                        val bio = ""+ds.child("bio").value
                        // ... others like phone nb or email if u want
                        // set user info
                        mUsernameTv.text = username
                        mNameTv.text = name
                        nbPostTv.text = nbPost
                        nbFollowingTv.text = nbFollowing
                        nbFollowersTv.text = nbFollowers
                        mBioTv.text = bio
                        // for picture:
                        try {
                            Picasso.get().load(image).into(mAvatarView)
                        }
                        catch (e: Exception) {
                            Picasso.get().load(R.drawable.profile_image).into(mAvatarView)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    /* I don't know if it's useful...
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    */

    override fun onStart() {
        // check on start of app
        checkUserStatus()
        super.onStart()
    }

    /* NOT USED
    /* inflate options menu */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // inflating enu
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /* handle menu item clicks */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // get item id
        val id : Int = item.itemId
        if (id == R.id.action_logout) {
            firebaseAuth.signOut()
            checkUserStatus()
        }
        return super.onOptionsItemSelected(item)
    }
    */
}
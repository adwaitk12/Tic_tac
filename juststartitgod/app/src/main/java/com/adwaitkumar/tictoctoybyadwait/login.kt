package com.adwaitkumar.tictoctoybyadwait

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class login : AppCompatActivity()
{
    private var mAuth: FirebaseAuth? = null
    private var database= FirebaseDatabase.getInstance()
    private var myref=database.reference
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
    }
    fun buloginevent(view: View)
    {
        logintofirebase(etemail.text.toString(),etpass.text.toString())
    }
    fun logintofirebase(email:String,pass:String)
    {
        mAuth!!.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this)
        {
            task->
            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"SUCCESS",Toast.LENGTH_LONG).show()
                var curuser=mAuth!!.currentUser
                // save in database
                //storing value  in my ref go to user(table) create one child(entry)
                if(curuser!=null)
                {
                    myref.child("users").child(splitstring(curuser.email.toString())).child("request").setValue(curuser!!.uid)
                }
                loadmain()
            }
            else
            {
                Toast.makeText(applicationContext,"FAIL",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadmain()
    }

    fun loadmain()
    {
        var curuser=mAuth!!.currentUser
        //
        if(curuser!=null)
        {
            var intent=Intent(this,MainActivity::class.java)
            intent.putExtra("email",curuser.email)
            intent.putExtra("uid",curuser.uid)
            startActivity(intent)
        }
    }
    fun splitstring(str:String):String
    {
        var split=str.split("@")
        return split[0]
    }
}


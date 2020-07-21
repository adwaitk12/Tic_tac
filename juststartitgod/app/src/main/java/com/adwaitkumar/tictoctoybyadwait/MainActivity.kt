package com.adwaitkumar.tictoctoybyadwait
import com.google.firebase.analytics.FirebaseAnalytics
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var database= FirebaseDatabase.getInstance()
    private var myref=database.reference
    private  var mFirebaseAnalytics:FirebaseAnalytics?=null
    var myemail:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this)

        var b: Bundle? =intent.extras
        if (b != null) {
            myemail=b.getString("email")
        }
        incomingcalls()
    }
    protected  fun butclick(view: View)
    {
        val buselected=view as Button
        var cellid=0
            /** which button is selected*/
        when (buselected.id)
        {
            R.id.bu1-> cellid=1
            R.id.bu2-> cellid=2
            R.id.bu3-> cellid=3
            R.id.bu4-> cellid=4
            R.id.bu5-> cellid=5
            R.id.bu6-> cellid=6
            R.id.bu7-> cellid=7
            R.id.bu8-> cellid=8
            R.id.bu9-> cellid=9

        }
        // creating session
        // activate the event listener on this session
        myref.child("playeronline").child(sessionid).child(cellid.toString()).setValue(myemail  )
    }
    var player1=ArrayList<Int>()
    var player2=ArrayList<Int>()
    var active=1;
    fun playgame(cellid:Int,buselselected:Button)
    {
        if(active==1)
        {
            buselselected.setBackgroundColor(Color.GREEN)
            buselselected.text="X"
            player1.add(cellid)
            active=2
        }
        else
        {
            buselselected.setBackgroundColor(Color.BLUE)
            buselselected.text="O"
            player2.add(cellid)
            active=1
        }
        buselselected.isEnabled=false
        checkwnner()
    }
    fun checkwnner()
    {
        var winner=-1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3))
        {
            winner=1;
        }
        if(player2.contains(1) && player2.contains(2) && player2.contains(3))
        {
            winner=2;
        }


        if(player1.contains(4) && player1.contains(5) && player1.contains(6))
        {
            winner=1;
        }
        if(player2.contains(4) && player2.contains(5) && player2.contains(6))
        {
            winner=2;
        }


        if(player1.contains(7) && player1.contains(8) && player1.contains(9))
        {
            winner=1;
        }
        if(player2.contains(7) && player2.contains(8) && player2.contains(9))
        {
            winner=2;
        }


        if(player1.contains(1) && player1.contains(4) && player1.contains(7))
        {
            winner=1;
        }
        if(player2.contains(1) && player2.contains(4) && player2.contains(7))
        {
            winner=2;
        }

        if(player1.contains(2) && player1.contains(5) && player1.contains(8))
        {
            winner=1;
        }
        if(player2.contains(2) && player2.contains(5) && player2.contains(8))
        {
            winner=2;
        }

        if(player1.contains(3) && player1.contains(6) && player1.contains(9))
        {
            winner=1;
        }
        if(player2.contains(3) && player2.contains(6) && player2.contains(9))
        {
            winner=2;
        }

        if(player1.contains(1) && player1.contains(5) && player1.contains(9))
        {
            winner=1;
        }
        if(player2.contains(1) && player2.contains(5) && player2.contains(9))
        {
            winner=2;
        }

        if(player1.contains(3) && player1.contains(5) && player1.contains(7))
        {
            winner=1;
        }
        if(player2.contains(3) && player2.contains(5) && player2.contains(7))
        {
            winner=2;
        }


        if(winner==1)
        {
            Toast.makeText(this,"PLAYER 1 IS THE WIINER",Toast.LENGTH_LONG).show()
        }
        if(winner==2)
        {
            Toast.makeText(this,"PLAYER 2 IS THE WIINER",Toast.LENGTH_LONG).show()
        }

    }

    fun autoplay(cellid: Int)
    {

        var curbutton:Button?
        when(cellid)
        {
            1->curbutton=bu1
            2->curbutton=bu2
            3->curbutton=bu3
            4->curbutton=bu4
            5->curbutton=bu5
            6->curbutton=bu6
            7->curbutton=bu7
            8->curbutton=bu8
            else->{
                curbutton=bu9
            }
        }
        playgame(cellid,curbutton)
    }
    protected fun burequestevent(view: View)
    {
        var curemail=etmail.text.toString()

        //creating session
        myref.child("users").child(curemail).child("request").push().setValue(myemail)
        playeronline(splitstring(myemail!!) + splitstring(etmail))
        playersymbol="X"
    }
    protected  fun buacceptevent(view: View)
    {
        var curemail=etmail.text.toString()
        // accepting is same as sending request so someone who sends request to me i request him back
        //creating seesion
        myref.child("users").child(curemail).child("request").push().setValue(myemail)
        playeronline(splitstring(etmail) + splitstring(myemail!!))
        playersymbol="O"
    }

    var sessionid:String?=null
    var playersymbol:String?=null

    fun playeronline(sessionid:String)
    {
        // on the first call by each player only the session is initialised
        this.sessionid=sessionid
        myref.child("playeronline").child(sessionid)
            .addValueEventListener(object : ValueEventListener{
                fun onDataChange(datasnapshot: DataSnapshot?)
                {
                    // executed on any datechange in request child datasnapshot is the current snapshot of data
                    try {
                        player1.clear()
                        player2.clear()
                        val td=datasnapshot as HashMap<String,Any>
                        if(td!=null)
                        {
                            var value:String
                            for(key in td.keys)
                            {
                                value=td[key] as String
                                // setting the text besides request button as the user who requested
                                if(value!=myemail)
                                {
                                    active=if(playersymbol==="X") 1 else 2
                                }
                                else
                                {
                                    active=if(playersymbol==="X") 2 else 1
                                }
                                autoplay(key.toInt())
                            }
                        }
                    }
                    catch (ex:Exception){}
                }

                fun onCancelled(p0: DatabaseError?) {

                }
            })
    }

    // for notification feature watch video
    
    // THE INCOMING CALLS TO ME WILL AFFECT THE REQUEST CHILD IN DATABASE SO CHEXKING EVERY VALUE BEING ADDED USING EVENT LISTENER
    fun incomingcalls()
    {
        myref.child("user").child(splitstring(myemail!!)).child("request")
            .addValueEventListener(object : ValueEventListener{
                fun onDataChange(datasnapshot: DataSnapshot?)
                {
                    // executed on any datechange in request child datasnapshot is the current snapshot of data
                    try {
                        val td=datasnapshot as HashMap<String,Any>
                        if(td!=null)
                        {
                            var value:String
                            for(key in td.keys)
                            {
                                value=td[key] as String
                                // setting the text besides request button as the user who requested
                                etmail.setText(value)
                                // clearing all other request
                                myref.child("user").child(splitstring(myemail!!)).child("request").setValue(true)
                                break;
                            }
                        }
                    }
                    catch (ex:Exception){}
                }

                fun onCancelled(p0: DatabaseError?) {

                }
            })
    }

    fun splitstring(str:String):String
    {
        var split=str.split("@")
        return split[0]
    }
}
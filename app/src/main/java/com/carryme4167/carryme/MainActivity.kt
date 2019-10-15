package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if ( user == null ) {
                val intent = Intent(this, LoginRegister::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else
            {
                var flagp: Int = 0
                var flagd: Int = 0
                val refp = FirebaseFirestore.getInstance().collection("Passenger")
                refp.addSnapshotListener { snap, e->
                    if ( e != null )
                    {
                        Log.d("CHECK", "${e.message}")
                    }
                    for ( doc in snap!! )
                    {
                        val temp = doc.toObject(User::class.java)
                        if ( temp.email.toString() == user.email.toString() )
                        {
                            flagp = 1
                            break
                        }
                    }
                    if ( flagp == 1 )
                    {
                        val intent = Intent(this, Passenger::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
                val refd = FirebaseFirestore.getInstance().collection("Driver")
                refd.addSnapshotListener { snap, e->
                    if ( e != null )
                    {
                        Log.d("CHECK", "${e.message}")
                    }
                    for ( doc in snap!! )
                    {
                        val temp = doc.toObject(User::class.java)
                        if ( temp.email.toString() == user.email.toString() )
                        {
                            flagd = 1
                            break
                        }
                    }
                    if ( flagd == 1 )
                    {
                        val intent = Intent(this, Driver::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        },3000)
    }
}

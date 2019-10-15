package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            val cat = findViewById<RadioButton>(radioSelctor.checkedRadioButtonId)
            val category = cat.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            login(email, password, category)
        }
    }

    private fun login(email: String, password: String, category: String)
    {
        if ( email.isEmpty() || password.isEmpty() )
        {
            Toast.makeText(this, "Please enter email address and password", Toast.LENGTH_SHORT).show()
        }
        else
        {
            var flag: Int = 0
            val ref = FirebaseFirestore.getInstance().collection("$category")
            Log.d("CHECK", "Checking $category database")
            ref.addSnapshotListener { snap, e ->
                if ( e != null )
                {
                    Log.d("CHECK", "${e.message}")
                }
                for ( doc in snap!! )
                {
                    val temp = doc.toObject(User::class.java)
                    if ( temp.email.toString() == email )
                    {
                        flag = 1
                        break
                    }
                }
                if ( flag == 1 ) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Successfully signed in. Directing to " + category + " page", Toast.LENGTH_SHORT).show()
                            if (category == "Driver") {
                                val intent = Intent(this, Driver::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            } else if (category == "Passenger") {
                                val intent = Intent(this, Passenger::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                        }
                }
                else
                {
                    Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


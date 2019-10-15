package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            val username = usernameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val phone = phoneText.text.toString()
            val nid = nidText.text.toString()
            val drivingLicence = drivingLicenceText.text.toString()

            val cat = findViewById<RadioButton>(radioSelctor.checkedRadioButtonId)
            val category = cat.text.toString()
            Log.d("DEETS", "$username $email $password $phone $nid $drivingLicence $category")
            register(username, email, password, phone, nid, category, drivingLicence)
        }
    }

    private fun register(username: String, email: String, password: String, phone: String, nid: String, category: String, drivingLicence: String)
    {
        if ( email.isEmpty() || password.isEmpty() || username.isEmpty() || phone.isEmpty() || nid.isEmpty() || category.isEmpty() )
        {
            Toast.makeText(this, "Please enter all details correctly before proceeding to register", Toast.LENGTH_SHORT).show()
        }
        else
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "$category $username created successfully", Toast.LENGTH_SHORT).show()
                    val uid = FirebaseAuth.getInstance().uid
                    val ref = FirebaseFirestore.getInstance().collection("$category")
                    val user = User(username, email, password, phone, nid, category, drivingLicence)
                    ref.document("$uid").set(user)
                        .addOnSuccessListener {
                            Log.d("Deets", "success")
                            Toast.makeText(this, "Added $category $username to database", Toast.LENGTH_SHORT).show()
                            val user_now = FirebaseAuth.getInstance().currentUser
                            val username_update = UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build()
                            user_now?.updateProfile(username_update)
                            user_now?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                                }
                            if ( category == "Driver" )
                            {
                                val intent = Intent(this, Driver::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                            else if ( category == "Passenger" )
                            {
                                val intent = Intent(this, Passenger::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error adding $category $username to database. ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }
}

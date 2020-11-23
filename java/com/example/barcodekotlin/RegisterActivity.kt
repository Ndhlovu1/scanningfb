package com.example.barcodekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

   lateinit var auth : FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Company Profiles")
        register()

        lgn_scrn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun register(){

        btn_reg.setOnClickListener {


            if (TextUtils.isEmpty(companyId.text.toString())){
                companyId.setText("Company Branch can't be empty!")
                return@setOnClickListener
            }else
                if (TextUtils.isEmpty(companyName.text.toString())){
                    companyName.setText("Company Email can't be empty!")
                    return@setOnClickListener
                }else
                    if (TextUtils.isEmpty(companyEmail.text.toString())){
                        companyEmail.setText("Company Email can't be empty!")
                        return@setOnClickListener
                    }else
                        if (TextUtils.isEmpty(companyPassword.text.toString())){
                            companyPassword.setText("Company Password can't be empty!")
                            return@setOnClickListener
                        }else
                            if (TextUtils.isEmpty(companyTel.text.toString())){
                                companyTel.setText("Company Password can't be empty!")
                                return@setOnClickListener
                            }else
                                if (TextUtils.isEmpty(companyLocation.text.toString())){
                                    companyLocation.setText("Company Password can't be empty!")
                                    return@setOnClickListener
                                }


            auth.createUserWithEmailAndPassword(companyEmail.text.toString(), companyPassword.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val currentUser = auth.currentUser
                        val currentUserDb = databaseReference?.child((currentUser?.uid!!))

                        currentUserDb?.child("Company Name")?.setValue(companyName.text.toString())


                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this, "Registration Success. Happy Scanning!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Registration Failed, please try again", Toast.LENGTH_SHORT).show()

                    }
                }
        }

    }




}

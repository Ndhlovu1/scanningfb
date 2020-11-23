package com.example.barcodekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        //Getting the current Session
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //go to Reg Activity if unregistered!
        go_to_reg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }


        btn_lgn.setOnClickListener {

            if (usr_name.text.trim().toString().isNotEmpty() && usr_pwd.text.trim().toString().isNotEmpty()) {
                login(usr_name.text.trim().toString(),usr_pwd.text.trim().toString())
            }

            else{
                Toast.makeText(this,"Input Required", Toast.LENGTH_LONG).show()
            }
        }


    }



    private fun login(email:String, password:String){

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->

                if (task.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                }

                else{
                    Toast.makeText(this, "Error Joining" + task.exception, Toast.LENGTH_SHORT).show()
                }



            }

    }


}

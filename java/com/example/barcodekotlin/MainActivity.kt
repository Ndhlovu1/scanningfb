package com.example.barcodekotlin

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.widget.Toast.makeText as makeText1

private const val CAMERA_REQUEST = 101

class MainActivity : AppCompatActivity() {


    lateinit var auth : FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    private lateinit var codeScanner: CodeScanner


    lateinit var option : Spinner
    lateinit var result : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Company Profiles")

        /*   Saving data to the cloud -> Winville Scanner
             var database = FirebaseDatabase.getInstance().reference
             database.setValue(tv_textView)
      */
        option = findViewById(R.id.spinner) as Spinner
        result = findViewById(R.id.sp_tView) as TextView

        val options = arrayOf("Select","Identity Document","Passport","Driver's","Student Card")

        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        //The DropDown
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
             result.text = "Please select an item"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.text = options.get(position)
            }

        }//End of Drop Down

        setupPermission()
        codeScanner()

    }//End of on Create

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Kotlin uses when instead of a Switch statement
        when(item.itemId){

            //Create the logging out features
            R.id.nav_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                return true

            }

        }

        return super.onOptionsItemSelected(item)
    }

    //End Of Menu


    //Code for performing Scanning ...
    private fun codeScanner() {

        codeScanner = CodeScanner(this, scan_screen)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
             runOnUiThread{
                 tv_textView.text = it.text

                 val user = auth.currentUser

                 var codeType = result.text.toString()
                 //Latest changes have been made below which shud allow for the data scanned to be automatically assigned to the object

                 //It is to assign each and variable to a particular user ...
                 val userReference = databaseReference?.child(user?.uid!!)?.child("Values")

                 //String artistId =   databaseReference.push().getKey();

                 //The above is to replace the below code
                 userReference?.child(codeType)?.setValue(it.text)

                // database.child(codeType.toString()).setValue(it.text)
                 Toast.makeText(applicationContext, "Your data has been saved", Toast.LENGTH_SHORT).show()
                }

            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
                }
            }

        }

        scan_screen.setOnClickListener {
            codeScanner.startPreview()
        }

    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makePermission()
        }

    }

    private fun makePermission(){
        ActivityCompat.requestPermissions( this , arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       when (requestCode){
           CAMERA_REQUEST -> {
               if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                   makeText1(this, "You need to grant camera permission to use this app. Else restart app to allow permissions.", Toast.LENGTH_LONG).show()
               }
           }
       }
    }

        }







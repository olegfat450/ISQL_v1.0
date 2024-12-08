package com.example.isql

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import java.util.Locale

class Activity2 : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var spinner: Spinner
    private lateinit var nameText: EditText
    private lateinit var phoneText: EditText
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var listTv: ListView

    private val db = DbHelper(this,null)
            val list: MutableList<String> = mutableListOf()
            var role = ""

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_2)
        
               init()
               val listadapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,list)
                   listTv.adapter = listadapter


           button2.setOnClickListener{

               if ( nameText.text.isEmpty() ) return@setOnClickListener

                val name = nameText.text.toString()
                val phone = phoneText.text.toString()
                    db.addName(name,phone,role)
               Toast.makeText(this,"${name} добавлен",Toast.LENGTH_LONG).show()
                    nameText.text.clear()
                    phoneText.text.clear() }


          button1.setOnClickListener {

                           list.clear()
                   val cursor = db.getInfo()
                       if ( cursor != null && cursor.moveToFirst() ) {
                           cursor.moveToFirst()
                           list += "${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)).capitalize()}  /  ${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_ROLE))}  /  ${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_PHONE))}"
                       }
                        while (cursor!!.moveToNext()) list += "${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME)).capitalize()}  /  ${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_ROLE))}  /  ${cursor.getString(cursor.getColumnIndex(DbHelper.KEY_PHONE))}"
                    listadapter.notifyDataSetChanged()
                   cursor.close() }

          button3.setOnClickListener { db.removeAll(); list.clear(); listadapter.notifyDataSetChanged(); Toast.makeText(this,"База очищена",Toast.LENGTH_LONG).show() }

        val itemSelected: AdapterView.OnItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {  role = parent?.getItemAtPosition(position) as String }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

        spinner.onItemSelectedListener = itemSelected


    }




       fun init(){

           button1 = findViewById(R.id.button1)
           button2 = findViewById(R.id.button2)
           button3 = findViewById(R.id.button3)
           phoneText = findViewById(R.id.phoneText)
           nameText = findViewById(R.id.nameText)
           spinner = findViewById(R.id.spiner)
           toolbar = findViewById(R.id.toolbar)
           listTv = findViewById(R.id.listTv)
           setSupportActionBar(toolbar)
           title = "База данных"
           toolbar.setTitleTextColor(Color.WHITE)
           toolbar.setBackgroundColor(Color.BLUE)
           toolbar.setTitleMarginStart(160)
           val spinerAdapter = ArrayAdapter.createFromResource(this,R.array.spiner,android.R.layout.simple_spinner_dropdown_item)
           spinner.adapter = spinerAdapter
       }



   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit,menu)
        return super.onCreateOptionsMenu(menu) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        AlertDialog.Builder(this).setTitle("Выход из программы").setMessage("Действительно выйти?")
            .setPositiveButton("Да") {s,v -> finishAffinity()}
            .setNegativeButton("Нет") {s,v -> s.cancel()}.create().show()

        return super.onOptionsItemSelected(item) }
}
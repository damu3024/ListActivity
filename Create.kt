package com.example.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*

class CreateMemoActivity: AppCompatActivity() {
    internal var helper: MemoOpenHelper? = null
    internal var newFlag = false
    internal var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        if (helper == null) {
            helper = MemoOpenHelper(this@CreateMemoActivity)
        }


        val intent = this.intent
        id = intent.getStringExtra("id").toString()
        if (id == "") {
            newFlag = true
        } else {
            val db = helper!!.writableDatabase
            try {
                val c = db.rawQuery("select body from MEMO_TABLE where uuid = '$id'", null)
                var next = c.moveToFirst()
                while (next) {
                    val dispBody = c.getString(0)
                    val body = findViewById<View>(R.id.body) as EditText
                    body.setText(dispBody, TextView.BufferType.NORMAL)
                    next = c.moveToNext()
                }
            } finally {
                db.close()
            }
        }
        val registerButton = findViewById<View>(R.id.register) as Button
        registerButton.setOnClickListener {
            val body = findViewById<View>(R.id.body) as EditText
            val bodyStr = body.text.toString()

            val db = helper!!.writableDatabase
            db.use { db ->
                if (newFlag) {
                    id = UUID.randomUUID().toString()
                    db.execSQL("insert into MEMO_TABLE(uuid, body) VALUES('$id', '$bodyStr')")
                } else {
                    db.execSQL("update MEMO_TABLE set body = '$bodyStr' where uuid = '$id'")
                }
            }
            val intent = Intent(this@CreateMemoActivity, ListActivity::class.java)
            startActivity(intent)
        }


        val backButton = findViewById<View>(R.id.back) as Button
        backButton.setOnClickListener {
            finish()
        }
    }


}

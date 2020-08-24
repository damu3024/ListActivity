package com.example.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class ListActivity : AppCompatActivity() {
    private var helper: MemoOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (helper == null) {
            helper = MemoOpenHelper(this@ListActivity)
        }
        val memoList = ArrayList<HashMap<String, String>>()
        val db = helper!!.writableDatabase
        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            val c = db.rawQuery("select uuid, body from MEMO_TABLE order by id", null)
            var next = c.moveToFirst()

            while (next) {
                val data = HashMap<String, String>()
                val uuid = c.getString(0)
                var body = c.getString(1)
                if (body.length > 10) {
                    // リストに表示するのは10文字まで
                    body = body.substring(0, 11) + "..."
                }
                data["body"] = body
                data["id"] = uuid
                memoList.add(data)
                next = c.moveToNext()
            }
        } finally {
            db.close()
        }

        val simpleAdapter = SimpleAdapter(this,
                memoList, // 使用するデータ
                android.R.layout.simple_list_item_2, // 使用するレイアウト
                arrayOf("body", "id"), // どの項目を
                intArrayOf(android.R.id.text1, android.R.id.text2) // どのidの項目に入れるか
        )

        val listView = findViewById<View>(R.id.memoList) as ListView
        listView.adapter = simpleAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val intent = Intent(this@ListActivity, CreateMemoActivity::class.java)

            val two = view as TwoLineListItem
            val idTextView = two.text2 as TextView
            val isStr = idTextView.text as String
            intent.putExtra("id", isStr)
            startActivity(intent)
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->

            val two = view as TwoLineListItem
            val idTextView = two.text2 as TextView
            val idStr = idTextView.text as String

            val db = helper!!.writableDatabase
            try {
                db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '$idStr'")
            } finally {
                db.close()
            }
            memoList.removeAt(position)
            simpleAdapter.notifyDataSetChanged()

            true
        }

        val newButton = findViewById<View>(R.id.newButton) as Button
        newButton.setOnClickListener {
            val intent = Intent(this@ListActivity, CreateMemoActivity::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }
    }
}

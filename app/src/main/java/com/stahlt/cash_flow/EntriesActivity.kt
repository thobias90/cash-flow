package com.stahlt.cash_flow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.stahlt.cash_flow.adapter.EntryAdapter
import com.stahlt.cash_flow.database.DatabaseHandler

class EntriesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btDelete: ImageView
    private lateinit var btEdit: ImageView
    private lateinit var database: DatabaseHandler
    private lateinit var entryAdapter: EntryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entries)

        database = DatabaseHandler(this)
        entryAdapter = EntryAdapter(this, database.listCursor())
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = entryAdapter

        btDelete = findViewById(R.id.btDelete)
        btEdit = findViewById(R.id.btEdit)

        btDelete.setOnClickListener {
            val index = entryAdapter.getSelectedItem()
            if (index == null) {
                Toast.makeText(this,
                    "You must SELECT one entry to DELETE", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)
                with(builder) {
                    setTitle("DELETE")
                    setMessage("Are you sure ?")
                    setPositiveButton("Yes") { _,_ ->
                        database.delete(index)
                        entryAdapter.updateCursor(database.listCursor())
                        entryAdapter.selectedHolder = null
                        recyclerView.adapter = entryAdapter
                    }
                    setNegativeButton("No") { _,_ ->
                        closeContextMenu()
                    }
                    show()
                }
            }
        }

        btEdit.setOnClickListener {
            val index = entryAdapter.getSelectedItem()
            if (index == null) {
                Toast.makeText(this,
                    "You must SELECT one entry to EDIT", Toast.LENGTH_SHORT).show()
            } else {
                val cashEntry = database.read(index)
                if (cashEntry != null) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", index)
                    intent.putExtra("type", cashEntry.type)
                    intent.putExtra("detail", cashEntry.detail)
                    intent.putExtra("date", cashEntry.date)
                    intent.putExtra("value", cashEntry.value)
                    startActivity(intent)
                }
            }
            Log.d("onEdit", "Position $index")
        }
    }

    override fun onRestart() {
        super.onRestart()
        entryAdapter.updateCursor(database.listCursor())
        entryAdapter.selectedHolder = null
        recyclerView.adapter = entryAdapter
    }
}
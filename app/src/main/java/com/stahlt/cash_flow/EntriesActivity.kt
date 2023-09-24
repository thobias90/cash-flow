package com.stahlt.cash_flow

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.stahlt.cash_flow.adapter.EntryAdapter
import com.stahlt.cash_flow.database.DatabaseHandler
import com.stahlt.cash_flow.entity.CashEntry
import java.lang.StringBuilder

class EntriesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btDelete: ImageView
    private lateinit var btEdit: ImageView
    private lateinit var database: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entries)

        database = DatabaseHandler(this)
        val entryAdapter = EntryAdapter(this, database.listCursor())
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = entryAdapter

        btDelete = findViewById(R.id.btDelete)
        btEdit = findViewById(R.id.btEdit)

        btDelete.setOnClickListener {
            var index = entryAdapter.getSelectedItem()
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
            var index = entryAdapter.getSelectedItem()
            if (index == null) {
                Toast.makeText(this,
                    "You must SELECT one entry to EDIT", Toast.LENGTH_SHORT).show()
            } else {
                index += 1
                val entry: CashEntry? = database.read(index)
                if (entry != null) {
                    val string = buildString {
                        append("$entry.type ")
                        append("$entry.detail ")
                        append("$entry.date ")
                        append(entry.value)
                    }
                    Log.d("..::EDIT::..",string)
                }
            }
            Log.d("onEdit", "Position $index")
        }
    }
}
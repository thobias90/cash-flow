package com.stahlt.cash_flow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.stahlt.cash_flow.adapter.EntryAdapter
import com.stahlt.cash_flow.database.DatabaseHandler

class EntriesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entries)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = EntryAdapter(this, DatabaseHandler(this).listCursor())
    }
}
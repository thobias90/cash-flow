package com.stahlt.cash_flow.adapter

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.stahlt.cash_flow.R

class EntryAdapter(private val context: Context, private val cursor: Cursor): RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvDate: TextView
        val tvDetail: TextView
        val tvValue: TextView
        val cardElement: CardView
        init {
            tvDate = view.findViewById(R.id.tvDate)
            tvDetail = view.findViewById(R.id.tvDetail)
            tvValue = view.findViewById(R.id.tvValue)
            cardElement = view.findViewById(R.id.cardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_element, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)

        holder.tvDate.text = cursor.getString(DATE_INDEX)
        holder.tvDetail.text = cursor.getString(DETAIL_INDEX)
        holder.tvValue.text = cursor.getString(VALUE_INDEX)

        if (cursor.getString(TYPE_INDEX) == "Credit") {
            holder.cardElement.setCardBackgroundColor("#8BC34A".toColorInt())
        } else {
            holder.cardElement.setCardBackgroundColor("#F44336".toColorInt())
        }

        holder.cardElement.setOnClickListener {
            Log.d("CARD", "onClick")
        }

        holder.cardElement.setOnLongClickListener {
            Log.d("CARD", "onLongClick")
            true
        }
    }

    companion object {
        private const val ID_INDEX = 0
        private const val TYPE_INDEX = 1
        private const val DETAIL_INDEX = 2
        private const val VALUE_INDEX = 3
        private const val DATE_INDEX = 4
    }
}
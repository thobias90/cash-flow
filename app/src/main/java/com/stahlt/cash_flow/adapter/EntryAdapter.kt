package com.stahlt.cash_flow.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R.*
import com.stahlt.cash_flow.R
import java.text.NumberFormat

class EntryAdapter(private val context: Context, private var cursor: Cursor) :
    RecyclerView.Adapter<EntryAdapter.ViewHolder>() {
    var selectedHolder: ViewHolder? = null
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView
        val tvDetail: TextView
        val tvValue: TextView
        val cardElement: CardView
        var primaryKey: Int?

        init {
            tvDate = view.findViewById(R.id.tvDate)
            tvDetail = view.findViewById(R.id.tvDetail)
            tvValue = view.findViewById(R.id.tvValue)
            cardElement = view.findViewById(R.id.cardView)
            primaryKey = null
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

        holder.primaryKey = cursor.getInt(ID_INDEX)
        holder.tvDate.text = cursor.getString(DATE_INDEX)
        holder.tvDetail.text = cursor.getString(DETAIL_INDEX)
        val numberFormat = NumberFormat.getCurrencyInstance()
        holder.tvValue.text = numberFormat.format(cursor.getString(VALUE_INDEX).toDouble())

        handleCardColor(holder, false)

        holder.cardElement.setOnClickListener {
            if (holder == selectedHolder) {
                handleCardColor(holder, false)
                selectedHolder = null
            }

        }

        holder.cardElement.setOnLongClickListener {
            if (selectedHolder == null) {
                selectedHolder = holder
            } else {
                handleCardColor(selectedHolder, false)
                selectedHolder = holder
            }
            handleCardColor(holder, true)
            true
        }
    }

    @SuppressLint("PrivateResource")
    private fun handleCardColor(holder: ViewHolder?, selected: Boolean) {
        val position = holder?.adapterPosition
        if (position != null) {
            cursor.moveToPosition(position)
        }

        if (selected) {
            holder?.cardElement?.setCardBackgroundColor("#28625B71".toColorInt())
            holder?.tvValue?.setTextColor(ContextCompat.getColor(context, color.m3_textfield_indicator_text_color))
            holder?.tvDetail?.setTextColor(ContextCompat.getColor(context, color.m3_textfield_indicator_text_color))
            holder?.tvDate?.setTextColor(ContextCompat.getColor(context, color.m3_textfield_indicator_text_color))
        } else {
            holder?.tvValue?.setTextColor(
                ContextCompat.getColor(context, color.m3_default_color_primary_text))
            holder?.tvDetail?.setTextColor(
                ContextCompat.getColor(context, color.m3_default_color_primary_text))
            holder?.tvDate?.setTextColor(
                ContextCompat.getColor(context, color.m3_default_color_primary_text))
            if (cursor.getString(TYPE_INDEX) == "Credit") {
                holder?.cardElement?.setCardBackgroundColor("#85bd87".toColorInt())
            } else {
                holder?.cardElement?.setCardBackgroundColor("#f1807e".toColorInt())
            }
        }
    }

    fun getSelectedItem(): Int? {
        return selectedHolder?.primaryKey
    }

    fun updateCursor(newCursor: Cursor) {
        cursor = newCursor
    }
    companion object {
        private const val ID_INDEX = 0
        private const val TYPE_INDEX = 1
        private const val DETAIL_INDEX = 2
        private const val VALUE_INDEX = 3
        private const val DATE_INDEX = 4
    }
}
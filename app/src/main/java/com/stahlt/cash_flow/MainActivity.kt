package com.stahlt.cash_flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.stahlt.cash_flow.database.DatabaseHandler
import com.stahlt.cash_flow.entity.CashEntry

class MainActivity : AppCompatActivity() {
    private lateinit var sCashType: Spinner
    private lateinit var sCashDetail: Spinner

    private lateinit var database: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Type Spinner
        sCashType = findViewById(R.id.cashType)
        sCashType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = p0?.getItemAtPosition(p2)
                if (item != null) {
                    setCashDetailData(item.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("..::TYPE SPINNER", "onNothingSelected")
            }
        }

        // Detail Spinner
        sCashDetail = findViewById(R.id.cashDetail)
        sCashDetail.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("..::DETAIL SPINNER", "onItemSelected")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("..::DETAIL SPINNER", "onNothingSelected")
            }

        }

        // Database Initialize
        database = DatabaseHandler(this)
    }

    fun setCashDetailData(type: String) {
        var arrayListId = R.array.debit_options
        if (type == "Credit") {
            arrayListId = R.array.credit_options
        }
        val detailAdapter = ArrayAdapter.createFromResource(
            this,
            arrayListId,
            android.R.layout.simple_spinner_item)
        detailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sCashDetail.adapter = detailAdapter
    }
}
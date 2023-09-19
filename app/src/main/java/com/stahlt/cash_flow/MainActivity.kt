package com.stahlt.cash_flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.stahlt.cash_flow.database.DatabaseHandler
import com.stahlt.cash_flow.entity.CashEntry

class MainActivity : AppCompatActivity() {
    private lateinit var sCashType: Spinner
    private lateinit var sCashDetail: Spinner
    private lateinit var etValue: EditText
    private lateinit var etDate: EditText

    private lateinit var database: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etValue = findViewById(R.id.etValue)
        etDate = findViewById(R.id.etDate)

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

    fun btAddOnClick(view: View) {
        val dateRegexPattern = Regex("([0-9]{2})/([0-9]{2})/([0-9]{4})")
        val type = sCashType.getItemAtPosition(sCashType.selectedItemPosition).toString()
        val detail = sCashDetail.getItemAtPosition(sCashDetail.selectedItemPosition).toString()
        if (etValue.text.isEmpty()) {
            etValue.error = "Value MUST be filled!"
        } else if (etDate.text.isEmpty()) {
            etDate.error = "Date MUST be filled!"
        } else if (!dateRegexPattern.matches(etDate.text.toString())) {
            etDate.error = "Date MUST be filled as dd/MM/yyyy"
        } else {
            val value = etValue.text.toString()
            val date = etDate.text.toString()
            val cashEntry = CashEntry(type, detail, value, date)
            database.create(cashEntry)
        }
    }
    fun btEntriesOnClick(view: View) {}
    fun btBalanceOnClick(view: View) {}
}
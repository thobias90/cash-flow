package com.stahlt.cash_flow

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.stahlt.cash_flow.database.DatabaseHandler
import com.stahlt.cash_flow.entity.CashEntry
import java.text.NumberFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var sCashType: Spinner
    private lateinit var sCashDetail: Spinner
    private lateinit var etValue: EditText
    private lateinit var etDate: EditText
    private lateinit var btBalance: Button
    private lateinit var btEntries: Button
    private lateinit var btAdd: Button

    private lateinit var database: DatabaseHandler
    private var editId: Int = 0
    private var selectedDetail: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        etValue = findViewById(R.id.etValue)
        etDate = findViewById(R.id.etDate)
        sCashType = findViewById(R.id.cashType)
        sCashDetail = findViewById(R.id.cashDetail)
        btBalance = findViewById(R.id.btBalance)
        btEntries = findViewById(R.id.btEntries)
        btAdd = findViewById(R.id.btAdd)

        editId = intent.getIntExtra("id", 0)
        if (editId != 0) {
            etValue.setText(intent.getStringExtra("value"))
            etDate.setText(intent.getStringExtra("date"))
            selectedDetail = intent.getStringExtra("detail")
            if (intent.getStringExtra("type") == "Credit") {
                sCashType.setSelection(0)
            } else {
                sCashType.setSelection(1)
            }
            btBalance.visibility = View.INVISIBLE
            btEntries.visibility = View.INVISIBLE
            btAdd.text = getString(R.string.update)
        }

        etDate.setOnClickListener {
            hideEtValueKeyboard(it)
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val date = "$day/${month+1}/$year"
                    etDate.setText(date)
                },
                year,
                month,
                day)
            datePickerDialog.show()
        }

        // Type Spinner
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
        if (selectedDetail != null) {
            when (selectedDetail) {
                "Salary" -> sCashDetail.setSelection(0)
                "Extra" -> sCashDetail.setSelection(1)
                "Food" -> sCashDetail.setSelection(0)
                "Transport" -> sCashDetail.setSelection(1)
                "Health" -> sCashDetail.setSelection(2)
                "Home" -> sCashDetail.setSelection(3)
            }
            selectedDetail = null
        }
    }

    fun btAddOnClick(view: View) {
        val type = sCashType.getItemAtPosition(sCashType.selectedItemPosition).toString()
        val detail = sCashDetail.getItemAtPosition(sCashDetail.selectedItemPosition).toString()
        if (etValue.text.isEmpty()) {
            etValue.error = "Value MUST be filled!"
        } else if (etDate.text.isEmpty()) {
            etDate.error = "Date MUST be filled!"
        } else {
            val value = etValue.text.toString()
            val date = etDate.text.toString()
            val cashEntry = CashEntry(type, detail, value, date)
            if (btAdd.text.toString() == "Update") {
                database.update(editId, cashEntry)
                etValue.setText("")
                etDate.setText("")
                hideEtValueKeyboard(view)
                Toast.makeText(this, "Register Modified", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                database.create(cashEntry)
                etValue.setText("")
                etDate.setText("")
                hideEtValueKeyboard(view)
                Toast.makeText(this, "Register Added", Toast.LENGTH_SHORT).show()
            }


        }
    }
    fun btEntriesOnClick(view: View) {
        val intent = Intent(this, EntriesActivity::class.java)
        startActivity(intent)
    }
    fun btBalanceOnClick(view: View) {
        hideEtValueKeyboard(view)
        val balance = database.getBalance()
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Balance")
            val numberFormat = NumberFormat.getCurrencyInstance()
            setMessage(numberFormat.format(balance))
            setPositiveButton("Ok") { _,_ -> closeContextMenu() }
            show()
        }
    }

    private fun hideEtValueKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
package com.example.currencyconverterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSource: EditText
    private lateinit var editTextTarget: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner
    private lateinit var convertButton: Button

    private var isSourceSelected: Boolean = true

    // Giả lập tỷ giá hối đoái
    private val currencyRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "VND" to 24000.0,
        "JPY" to 110.0,
        "GBP" to 0.75
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các thành phần giao diện
        editTextSource = findViewById(R.id.editTextSource)
        editTextTarget = findViewById(R.id.editTextTarget)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)
        convertButton = findViewById(R.id.convertButton)

        // Khởi tạo danh sách tiền tệ cho Spinner
        val currencies = listOf("USD", "EUR", "VND", "JPY", "GBP")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter

        // Sự kiện chọn đồng tiền nguồn và đích
        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSourceSelected) {
                    convertCurrency()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTargetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isSourceSelected) {
                    convertCurrency()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Sự kiện khi chọn EditText
        editTextSource.setOnFocusChangeListener { _, hasFocus ->
            isSourceSelected = hasFocus
        }
        editTextTarget.setOnFocusChangeListener { _, hasFocus ->
            isSourceSelected = !hasFocus
        }

        // Sự kiện nhấn nút "Chuyển đổi"
        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    // Hàm chuyển đổi tiền tệ
    private fun convertCurrency() {
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val targetCurrency = spinnerTargetCurrency.selectedItem.toString()

        val sourceAmount = editTextSource.text.toString().toDoubleOrNull() ?: 0.0
        val sourceRate = currencyRates[sourceCurrency] ?: 1.0
        val targetRate = currencyRates[targetCurrency] ?: 1.0

        val targetAmount = sourceAmount * (targetRate / sourceRate)
        editTextTarget.setText(String.format("%.2f", targetAmount))
    }
}

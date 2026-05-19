package com.neonloop.nongsalog

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WriteFarmLogActivity : Activity() {

    private lateinit var tvWriteDate: TextView
    private lateinit var spinnerCrop: Spinner
    private lateinit var spinnerField: Spinner
    private lateinit var spinnerWorkType: Spinner
    private lateinit var editContent: EditText
    private lateinit var editCost: EditText
    private lateinit var editHarvestAmount: EditText
    private lateinit var btnSaveFarmLog: Button
    private lateinit var btnBack: Button

    private val cropList = listOf(
        "작물 선택",
        "고추",
        "상추",
        "토마토",
        "감자",
        "고구마",
        "배추",
        "오이",
        "기타"
    )

    private val fieldList = listOf(
        "밭 / 구역 선택",
        "앞마당 텃밭",
        "비닐하우스 1동",
        "고추밭",
        "상추밭",
        "기타"
    )

    private val workTypeList = listOf(
        "작업 종류 선택",
        "파종",
        "물주기",
        "비료",
        "방제",
        "제초",
        "수확",
        "관찰",
        "기타"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_farm_log)

        initViews()
        setupTodayDate()
        setupSpinners()
        setupClickListeners()
    }

    private fun initViews() {
        tvWriteDate = findViewById(R.id.tvWriteDate)
        spinnerCrop = findViewById(R.id.spinnerCrop)
        spinnerField = findViewById(R.id.spinnerField)
        spinnerWorkType = findViewById(R.id.spinnerWorkType)
        editContent = findViewById(R.id.editContent)
        editCost = findViewById(R.id.editCost)
        editHarvestAmount = findViewById(R.id.editHarvestAmount)
        btnSaveFarmLog = findViewById(R.id.btnSaveFarmLog)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupTodayDate() {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN)
        tvWriteDate.text = dateFormat.format(Date())
    }

    private fun setupSpinners() {
        spinnerCrop.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            cropList
        )

        spinnerField.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            fieldList
        )

        spinnerWorkType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            workTypeList
        )
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSaveFarmLog.setOnClickListener {
            saveFarmLog()
        }
    }

    private fun saveFarmLog() {
        val dateText = tvWriteDate.text.toString()
        val cropName = spinnerCrop.selectedItem.toString()
        val fieldName = spinnerField.selectedItem.toString()
        val workType = spinnerWorkType.selectedItem.toString()
        val content = editContent.text.toString().trim()
        val cost = editCost.text.toString().trim()
        val harvestAmount = editHarvestAmount.text.toString().trim()

        if (cropName == "작물 선택") {
            Toast.makeText(this, "작물을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (fieldName == "밭 / 구역 선택") {
            Toast.makeText(this, "밭 / 구역을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (workType == "작업 종류 선택") {
            Toast.makeText(this, "작업 종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "작업 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        FarmLogStorageUtil.saveLog(
            context = this,
            dateText = dateText,
            cropName = cropName,
            fieldName = fieldName,
            workType = workType,
            content = content,
            cost = cost,
            harvestAmount = harvestAmount
        )

        Toast.makeText(this, "농사일지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
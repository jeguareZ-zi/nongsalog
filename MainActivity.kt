package com.neonloop.nongsalog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var tvToday: TextView
    private lateinit var tvCropCount: TextView
    private lateinit var tvMonthWorkCount: TextView
    private lateinit var tvRecentLog1: TextView
    private lateinit var tvRecentLog2: TextView

    private lateinit var btnWriteLog: Button
    private lateinit var btnLogList: Button
    private lateinit var btnCropManage: Button
    private lateinit var btnFieldManage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupTodayDate()
        setupClickListeners()
        updateDashboard()
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun initViews() {
        tvToday = findViewById(R.id.tvToday)
        tvCropCount = findViewById(R.id.tvCropCount)
        tvMonthWorkCount = findViewById(R.id.tvMonthWorkCount)
        tvRecentLog1 = findViewById(R.id.tvRecentLog1)
        tvRecentLog2 = findViewById(R.id.tvRecentLog2)

        btnWriteLog = findViewById(R.id.btnWriteLog)
        btnLogList = findViewById(R.id.btnLogList)
        btnCropManage = findViewById(R.id.btnCropManage)
        btnFieldManage = findViewById(R.id.btnFieldManage)
    }

    private fun setupTodayDate() {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN)
        val todayText = dateFormat.format(Date())
        tvToday.text = "오늘 날짜  |  $todayText"
    }

    private fun setupClickListeners() {
        btnWriteLog.setOnClickListener {
            val intent = Intent(this, WriteFarmLogActivity::class.java)
            startActivity(intent)
        }

        btnLogList.setOnClickListener {
            val intent = Intent(this, FarmLogListActivity::class.java)
            startActivity(intent)
        }

        btnCropManage.setOnClickListener {
            val intent = Intent(this, CropManageActivity::class.java)
            startActivity(intent)
        }

        btnFieldManage.setOnClickListener {
            Toast.makeText(this, "밭 / 구역 관리 화면으로 이동 예정", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDashboard() {
        val logs = FarmLogStorageUtil.loadAllLogs(this)

        tvMonthWorkCount.text = logs.size.toString()
        val crops = CropStorageUtil.loadAllCrops(this)
        tvCropCount.text = crops.size.toString()

        if (logs.isEmpty()) {
            tvRecentLog1.text = "아직 작성된 농사일지가 없습니다."
            tvRecentLog2.text = "작물을 등록하고 첫 기록을 남겨보세요."
            return
        }

        val firstLog = logs.getOrNull(0)
        val secondLog = logs.getOrNull(1)

        tvRecentLog1.text = firstLog?.let { makeRecentLogText(it) }
            ?: "아직 작성된 농사일지가 없습니다."

        tvRecentLog2.text = secondLog?.let { makeRecentLogText(it) }
            ?: "두 번째 농사일지는 아직 없습니다."
    }

    private fun makeRecentLogText(log: FarmLogMemo): String {
        return """
            ${log.dateText}
            ${log.cropName} / ${log.fieldName} / ${log.workType}
            ${log.content}
            비용: ${log.cost}원 | 수확량: ${log.harvestAmount}
        """.trimIndent()
    }
}
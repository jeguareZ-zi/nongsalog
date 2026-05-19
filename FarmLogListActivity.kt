package com.neonloop.nongsalog

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class FarmLogListActivity : Activity() {

    private lateinit var tvLogListCount: TextView
    private lateinit var logContainer: LinearLayout
    private lateinit var btnBackFromList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_log_list)

        initViews()
        setupClickListeners()
        loadFarmLogs()
    }

    override fun onResume() {
        super.onResume()
        loadFarmLogs()
    }

    private fun initViews() {
        tvLogListCount = findViewById(R.id.tvLogListCount)
        logContainer = findViewById(R.id.logContainer)
        btnBackFromList = findViewById(R.id.btnBackFromList)
    }

    private fun setupClickListeners() {
        btnBackFromList.setOnClickListener {
            finish()
        }
    }

    private fun loadFarmLogs() {
        val logs = FarmLogStorageUtil.loadAllLogs(this)

        tvLogListCount.text = "전체 ${logs.size}개 기록"

        logContainer.removeAllViews()

        if (logs.isEmpty()) {
            val emptyTextView = makeLogTextView(
                """
                아직 저장된 농사일지가 없습니다.
                
                홈 화면에서 '오늘 농사일지 작성하기'를 눌러
                첫 농사 기록을 남겨보세요.
                """.trimIndent()
            )
            logContainer.addView(emptyTextView)
            return
        }

        logs.forEach { log ->
            val logTextView = makeLogTextView(makeLogText(log))
            logContainer.addView(logTextView)
        }
    }

    private fun makeLogText(log: FarmLogMemo): String {
        return """
            ${log.dateText}
            
            작물: ${log.cropName}
            구역: ${log.fieldName}
            작업: ${log.workType}
            
            내용:
            ${log.content}
            
            비용: ${log.cost}원
            수확량: ${log.harvestAmount}
        """.trimIndent()
    }

    private fun makeLogTextView(text: String): TextView {
        val textView = TextView(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)

        textView.layoutParams = params
        textView.text = text
        textView.textSize = 15f
        textView.setTextColor(android.graphics.Color.parseColor("#263A24"))
        textView.setBackgroundColor(android.graphics.Color.WHITE)
        textView.setPadding(18, 18, 18, 18)

        return textView
    }
}
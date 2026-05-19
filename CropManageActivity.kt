package com.neonloop.nongsalog

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class CropManageActivity : Activity() {

    private lateinit var editCropName: EditText
    private lateinit var editCropVariety: EditText
    private lateinit var editCropMemo: EditText
    private lateinit var btnSaveCrop: Button
    private lateinit var btnBackFromCrop: Button
    private lateinit var tvCropManageCount: TextView
    private lateinit var cropContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_manage)

        initViews()
        setupClickListeners()
        loadCrops()
    }

    override fun onResume() {
        super.onResume()
        loadCrops()
    }

    private fun initViews() {
        editCropName = findViewById(R.id.editCropName)
        editCropVariety = findViewById(R.id.editCropVariety)
        editCropMemo = findViewById(R.id.editCropMemo)
        btnSaveCrop = findViewById(R.id.btnSaveCrop)
        btnBackFromCrop = findViewById(R.id.btnBackFromCrop)
        tvCropManageCount = findViewById(R.id.tvCropManageCount)
        cropContainer = findViewById(R.id.cropContainer)
    }

    private fun setupClickListeners() {
        btnSaveCrop.setOnClickListener {
            saveCrop()
        }

        btnBackFromCrop.setOnClickListener {
            finish()
        }
    }

    private fun saveCrop() {
        val name = editCropName.text.toString().trim()
        val variety = editCropVariety.text.toString().trim()
        val memo = editCropMemo.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "작물명을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        CropStorageUtil.saveCrop(
            context = this,
            name = name,
            variety = variety,
            memo = memo
        )

        Toast.makeText(this, "작물이 등록되었습니다.", Toast.LENGTH_SHORT).show()

        editCropName.text.clear()
        editCropVariety.text.clear()
        editCropMemo.text.clear()

        loadCrops()
    }

    private fun loadCrops() {
        val crops = CropStorageUtil.loadAllCrops(this)

        tvCropManageCount.text = "등록된 작물 ${crops.size}개"

        cropContainer.removeAllViews()

        if (crops.isEmpty()) {
            val emptyView = makeCropTextView(
                """
                아직 등록된 작물이 없습니다.
                
                작물명을 입력하고 등록해보세요.
                """.trimIndent()
            )
            cropContainer.addView(emptyView)
            return
        }

        crops.forEach { crop ->
            val cropView = makeCropTextView(makeCropText(crop))
            cropContainer.addView(cropView)
        }
    }

    private fun makeCropText(crop: CropMemo): String {
        return """
            작물명: ${crop.name}
            품종: ${crop.variety}
            메모: ${crop.memo}
        """.trimIndent()
    }

    private fun makeCropTextView(text: String): TextView {
        val textView = TextView(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)

        textView.layoutParams = params
        textView.text = text
        textView.textSize = 15f
        textView.setTextColor(Color.parseColor("#263A24"))
        textView.setBackgroundColor(Color.WHITE)
        textView.setPadding(18, 18, 18, 18)

        return textView
    }
}
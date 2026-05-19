package com.neonloop.nongsalog

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class CropMemo(
    val id: String,
    val name: String,
    val variety: String,
    val memo: String,
    val createdAt: Long
)

object CropStorageUtil {

    private const val PREF_NAME = "nongsalog_crop_storage"
    private const val KEY_CROPS = "crops"

    fun saveCrop(
        context: Context,
        name: String,
        variety: String,
        memo: String
    ) {
        val crops = loadAllCrops(context).toMutableList()

        val newCrop = CropMemo(
            id = UUID.randomUUID().toString(),
            name = name,
            variety = variety.ifEmpty { "품종 미입력" },
            memo = memo.ifEmpty { "메모 없음" },
            createdAt = System.currentTimeMillis()
        )

        crops.add(0, newCrop)
        saveAllCrops(context, crops)
    }

    fun loadAllCrops(context: Context): List<CropMemo> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonText = prefs.getString(KEY_CROPS, "[]") ?: "[]"

        val result = mutableListOf<CropMemo>()

        try {
            val jsonArray = JSONArray(jsonText)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val crop = CropMemo(
                    id = obj.optString("id"),
                    name = obj.optString("name"),
                    variety = obj.optString("variety"),
                    memo = obj.optString("memo"),
                    createdAt = obj.optLong("createdAt")
                )

                result.add(crop)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun saveAllCrops(context: Context, crops: List<CropMemo>) {
        val jsonArray = JSONArray()

        crops.forEach { crop ->
            val obj = JSONObject()
            obj.put("id", crop.id)
            obj.put("name", crop.name)
            obj.put("variety", crop.variety)
            obj.put("memo", crop.memo)
            obj.put("createdAt", crop.createdAt)
            jsonArray.put(obj)
        }

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_CROPS, jsonArray.toString())
            .apply()
    }
}
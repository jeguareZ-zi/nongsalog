package com.neonloop.nongsalog

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class FarmLogMemo(
    val id: String,
    val dateText: String,
    val cropName: String,
    val fieldName: String,
    val workType: String,
    val content: String,
    val cost: String,
    val harvestAmount: String,
    val createdAt: Long
)

object FarmLogStorageUtil {

    private const val PREF_NAME = "nongsalog_farm_log_storage"
    private const val KEY_LOGS = "farm_logs"

    fun saveLog(
        context: Context,
        dateText: String,
        cropName: String,
        fieldName: String,
        workType: String,
        content: String,
        cost: String,
        harvestAmount: String
    ) {
        val logs = loadAllLogs(context).toMutableList()

        val newLog = FarmLogMemo(
            id = UUID.randomUUID().toString(),
            dateText = dateText,
            cropName = cropName,
            fieldName = fieldName,
            workType = workType,
            content = content,
            cost = cost.ifEmpty { "0" },
            harvestAmount = harvestAmount.ifEmpty { "없음" },
            createdAt = System.currentTimeMillis()
        )

        logs.add(0, newLog)
        saveAllLogs(context, logs)
    }

    fun loadAllLogs(context: Context): List<FarmLogMemo> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonText = prefs.getString(KEY_LOGS, "[]") ?: "[]"

        val result = mutableListOf<FarmLogMemo>()

        try {
            val jsonArray = JSONArray(jsonText)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val log = FarmLogMemo(
                    id = obj.optString("id"),
                    dateText = obj.optString("dateText"),
                    cropName = obj.optString("cropName"),
                    fieldName = obj.optString("fieldName"),
                    workType = obj.optString("workType"),
                    content = obj.optString("content"),
                    cost = obj.optString("cost"),
                    harvestAmount = obj.optString("harvestAmount"),
                    createdAt = obj.optLong("createdAt")
                )

                result.add(log)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun saveAllLogs(context: Context, logs: List<FarmLogMemo>) {
        val jsonArray = JSONArray()

        logs.forEach { log ->
            val obj = JSONObject()
            obj.put("id", log.id)
            obj.put("dateText", log.dateText)
            obj.put("cropName", log.cropName)
            obj.put("fieldName", log.fieldName)
            obj.put("workType", log.workType)
            obj.put("content", log.content)
            obj.put("cost", log.cost)
            obj.put("harvestAmount", log.harvestAmount)
            obj.put("createdAt", log.createdAt)

            jsonArray.put(obj)
        }

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_LOGS, jsonArray.toString())
            .apply()
    }
}
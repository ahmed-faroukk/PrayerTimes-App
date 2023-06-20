package com.example.alamiya_task.common.util

import java.text.SimpleDateFormat
import java.util.Locale


fun String.formatTimeTo12Hour(): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    return try {
        val date = inputFormat.parse(this)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        this // Return the original string if parsing fails
    }
}

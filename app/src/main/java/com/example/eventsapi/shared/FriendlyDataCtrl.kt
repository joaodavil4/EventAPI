package com.example.eventsapi.shared

import java.text.SimpleDateFormat

class FriendlyDataCtrl {


    fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = s
            return sdf.format(netDate)
        } catch (e: Exception) {
            return s
        }
    }


}
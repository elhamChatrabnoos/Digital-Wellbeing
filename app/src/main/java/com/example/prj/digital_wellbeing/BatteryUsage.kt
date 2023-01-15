package com.example.prj.digital_wellbeing

import android.annotation.SuppressLint
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BatteryUsage (context: Context){

    private val theContext = context

    // before everything init run
    init {
        if(getUsageStateList().isEmpty()){
            // if permission is unable go to setting
            val settingIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(settingIntent)
        }
    }

    @SuppressLint("WrongConstant")
    fun getUsageStateList(): MutableList<UsageStats> {
        //  to get list of app name and their battery usage from system
        var date = Date()
        var hour = SimpleDateFormat("hh", Locale.getDefault()).format(date).toInt()
        var minute = SimpleDateFormat("mm", Locale.getDefault()).format(date).toInt()
        val startTime = System.currentTimeMillis() - ((hour * 60 * 60 * 1000) + (minute * 1000 * 60))
        val endTime = System.currentTimeMillis()
        val usm = theContext.getSystemService("usagestats") as UsageStatsManager
        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    }

    // to get usage time of all applications
    fun getAppsTime():Double{
        var totalAppsTime : Double = 0.0
        var usageList = getUsageStateList()
        if (usageList != null) {
            for (item in usageList){
                if (item.totalTimeInForeground > 1000 * 60){
                    totalAppsTime += item.totalTimeInForeground
                    Log.d("2020", item.packageName)
                }
            }
        }
        Log.d("2020", "bbbbbbbbbbbbbbb")

        return totalAppsTime
    }
}
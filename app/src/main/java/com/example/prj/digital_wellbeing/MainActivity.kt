package com.example.prj.digital_wellbeing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prj.digital_wellbeing.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

     lateinit var binding : ActivityMainBinding
    var appList : List<AppModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
       getAppsNameAndTime()

    }

    fun getAppsNameAndTime(){
        // get each app name and time
        val batteryUsage = BatteryUsage(this)
        val batteryUsageList = batteryUsage.getUsageStateList()

        for (item in batteryUsageList!!){
            if (item.totalTimeInForeground > 60 * 1000){
                var ap = AppModel()
                ap.appName = item.packageName
                var bu = item.totalTimeInForeground
                ap.appTime = bu.toString()
                ap.appBatteryUsage = (bu.toFloat() / batteryUsage.getAppsTime().toFloat()*100).toInt()
                appList += ap
            }
        }

        var appsAdapter = AppsAdapter(appList, this, batteryUsage.getAppsTime())
        binding.appsListRecycler.setHasFixedSize(true)
        binding.appsListRecycler.layoutManager = LinearLayoutManager(this)
        binding.appsListRecycler.adapter = appsAdapter

        showTotalUsed(batteryUsage)
    }


    private fun showTotalUsed(batteryUsage:BatteryUsage){
        val totalTime = batteryUsage.getAppsTime()
        var hour  =  (totalTime / 1000 / 60 / 60).toInt()
        var minute = (totalTime / 1000 / 60 % 60).roundToInt()

        binding.totalAppsTime.text = "$hour hour $minute min"
    }
}
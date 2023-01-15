package com.example.prj.digital_wellbeing
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt
import kotlin.time.seconds

class AppsAdapter(
    var inputList: List<AppModel>,
    var context : Context,
    var totalTime: Double ) : RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    private var appsList : List<AppModel> = ArrayList()

    init {
         appsList = sortedUsage(inputList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layout : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_list_recycler, parent, false)
        return AppViewHolder(layout)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.appName.text = getAppName(appsList[position].appName.toString())
        holder.appTime.text = appsList[position].appTime
        holder.appPercent.text = appsList[position].appBatteryUsage.toString() + "%"
        holder.progress.progress = appsList[position].appBatteryUsage!!
        holder.appIcon.setImageDrawable(getAppIcon(appsList[position].appName.toString()))

    }

    override fun getItemCount(): Int {
        return appsList.size
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var appName : TextView = itemView.findViewById(R.id.app_name)
        var appTime : TextView = itemView.findViewById(R.id.app_time)
        var appPercent : TextView = itemView.findViewById(R.id.app_percent)
        var appIcon : ImageView = itemView.findViewById(R.id.app_icon)
        var progress : ProgressBar = itemView.findViewById(R.id.progress_used)
    }

    private fun getAppName(packageName: String): String{
        val packageMan = context.applicationContext.packageManager
        val appInfo : ApplicationInfo? =
            try {
            packageMan.getApplicationInfo(packageName, 0)
        } catch (e:PackageManager.NameNotFoundException){
            null
        }
        return (if (appInfo != null) packageMan.getApplicationLabel(appInfo) else ("unknown"))  as String
    }


    private fun getAppIcon(packageName: String): Drawable? {
        var icon : Drawable? = null
        try {
           icon = context.packageManager.getApplicationIcon(packageName)
        }
        catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }
        return icon
    }


    private fun sortedUsage(theList : List<AppModel>): List<AppModel>{
        var finalList : List<AppModel> = ArrayList()
        val sortedList =
            theList
//                .groupBy { it.appName }
//                .mapValues { entry -> entry.value.sumBy { it.appBatteryUsage!! } }.toList()
                .sortedWith(compareBy{it.appBatteryUsage}).reversed()

        var appNAME = arrayListOf("")

        // time per minute
        for( item in sortedList){
            if (item.appName in appNAME){
                continue
            }
            val appModel = AppModel()
            val appTime = item.appTime!!.toFloat()
            val hour = (appTime / 1000 / 60 / 60).toInt()
            val minute = (appTime /1000 / 60 % 60).roundToInt()
//            val appTime = item.second.toFloat() / 100 * totalTime.toFloat() / 1000 / 60
//            val hour = (appTime / 60).toInt()
//            val minute = (appTime % 60).roundToInt()
            appModel.appTime = "$hour hour $minute min"
            appModel.appName = item.appName
            appNAME.add(item.appName.toString())
            appModel.appBatteryUsage = item.appBatteryUsage
            finalList += appModel
        }
        return finalList
    }
}
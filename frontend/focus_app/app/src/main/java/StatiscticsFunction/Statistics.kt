package StatiscticsFunction

import ApiRequest.*

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.focus_app.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Calendar
 suspend fun asyncActivityYearly(catName:String,activityName: String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsActivityYearly("test", catName, activityName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncActivityDaily(catName:String,activityName: String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsActivityDaily("test", catName, activityName) { time ->
            continuation.resume(time, null)
        }
    }
}

 suspend fun asyncActivityWeekly(catName:String,activityName: String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsActivityWeekly("test", catName, activityName) { time ->
            continuation.resume(time , null)
        }
    }
}
 suspend fun asyncActivityTotally(catName:String, activityName: String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsAcrivityTotaly("test", catName, activityName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncActivityMonthly(catName:String,activityName: String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsActivityMonthly("test", catName, activityName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncCategoryDaily(catName:String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsCategoryDaily("test", catName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncCategoryWeekly(catName:String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsCategoryWeekly("test", catName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncCategoryMonthly(catName:String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsCategoryMonthly("test", catName) { time ->
            continuation.resume(time , null)
        }
    }
}

 suspend fun asyncCategoryYearly(catName:String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsCategoryYearly("test", catName) { time ->
            continuation.resume(time , null)
        }
    }
}
 suspend fun asyncCategoryTotally(catName:String): Float {
    return suspendCancellableCoroutine { continuation ->
        GetStatisticsCategoryTotaly("test", catName) { time ->
            continuation.resume(time , null)
        }
    }
}
fun updatePieChart(string: String,context: Context, pieChart: PieChart, timeProcent:MutableList<Float>, NamesStatisticList:MutableList<String>) {

    val entries = mutableListOf<PieEntry>()
    for(i in NamesStatisticList.indices)
    {
        entries.add(PieEntry(timeProcent[i],NamesStatisticList[i]))
    }
    val dataSet = PieDataSet(entries,  string)
    dataSet.colors = listOf(
        getColor(context,R.color.yellow),
        getColor(context,R.color.blue),
        getColor(context,R.color.red1),
        getColor(context,R.color.red2),
        getColor(context,R.color.red)
    )

    val legend = pieChart.legend
    legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    legend.orientation = Legend.LegendOrientation.VERTICAL
    legend.setDrawInside(false)
    legend.textSize = 20f // Set the text size for the legend
    legend.textColor = ContextCompat.getColor(context, R.color.black) // Set the text color for the legend

    legend.typeface = Typeface.DEFAULT_BOLD

    legend.formSize=20f

    // Customize the value text color and size
    dataSet.valueTextColor =R.color.black // Set this to your desired color for numbers
    dataSet.valueTextSize = 15f

    dataSet.setValueTypeface(Typeface.DEFAULT_BOLD) // Set the value text to be bold


    val data = PieData(dataSet)
    pieChart.data = data
    pieChart.invalidate()
}


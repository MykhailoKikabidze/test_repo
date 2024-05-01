package StatisticCharts

import ApiRequest.GetCategory
import Data.categoriesNames
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.CalendarView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.focus_app.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class PieChart : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pie_chart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pie_chart_main_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pieChart = findViewById<PieChart>(R.id.pie_chart)
        val calendarView=findViewById<CalendarView>(R.id.calendar)


        drawerLayout=findViewById(R.id.toolbar_main)

        val navigationView=findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar)
        val toggle= ActionBarDrawerToggle(this,drawerLayout, toolbar ,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        calendarView.setDate(currentDate.timeInMillis)

        val entries = mutableListOf<PieEntry>()

        val values = listOf(30f, 20f, 10f, 20f, 20f)

        for (i in categoriesNames.indices) {
            entries.add(PieEntry(values[i], categoriesNames[i]))
        }


        val dataSet = PieDataSet(entries, "Pie Chart")
        dataSet.colors = listOf(
            getColor(R.color.yellow),
            getColor(R.color.blue),
            getColor(R.color.red)
        )

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(android.R.color.transparent)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.invalidate()

//        calendarView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                // Calculate 70% of the screen width
//                val screenWidth = resources.displayMetrics.widthPixels
//                val calendarWidth = (screenWidth * 0.7).toInt()
//
//                // Set the width of the CalendarView to 70% of the screen width
//                calendarView.layoutParams.width = calendarWidth
//
//                // Remove the layout listener to avoid multiple calls
//                calendarView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//            }
//        })


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_menu -> {
                Toast.makeText(this, "wowo", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.settings_menu -> {
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.statistics_menu -> {
                Toast.makeText(this, "static", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed(){
        super.onBackPressed()
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
package StatisticCharts

import ApiRequest.*
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import java.util.jar.Attributes.Name

class PieChart : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    var isPeriodChanged=false
    private lateinit var toolbar: Toolbar
    private lateinit var categoryDisplayed:TextView
    private lateinit var pieChart:PieChart
    private lateinit var calendarView:CalendarView
    private lateinit var savedPeriod:String
    private lateinit var NamesStaticList: MutableList<String>
    // var activityNamesStati= mutableListOf<String>()
    private  lateinit var categoryName:String
    var timeProcent: MutableList<Float> = mutableListOf()
    private lateinit var periodToDisplay:TextView

    private var way = mutableListOf<PieEntry>()
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
        pieChart = findViewById<PieChart>(R.id.pie_chart)
        calendarView = findViewById<CalendarView>(R.id.calendar)

        periodToDisplay = findViewById<TextView>(R.id.periodTime)
        periodToDisplay.text=intent.extras?.getString("period")?:"Daily>"

        categoryDisplayed = findViewById<TextView>(R.id.selectCategory)





        savedPeriod = getSavedPeriod()?:"Daily>"
        if (savedPeriod != null) {
            periodToDisplay.text = savedPeriod
        }
        val savedCategory = getSavedCategory()







        categoryName = intent.extras?.getString("categoryName") ?:"Category>"
        if(categoryName!=" " && categoryName!="Category>")
        {
            categoryDisplayed.text=categoryName
            intent.removeExtra("categoryName")
            saveCategory(categoryName)
        }
        else{
            categoryDisplayed.text=savedCategory
            categoryName=savedCategory?:"Total>"
        }
        categoryDisplayed.setOnClickListener() {
            ChooseCategoryDisplay(categoryDisplayed)
        }


        DisplayChart()


        drawerLayout = findViewById(R.id.toolbar_chart_main)

        val navigationView = findViewById<NavigationView>(R.id.nav_view_chart)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_chart)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        calendarView.setDate(currentDate.timeInMillis)


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


    fun DisplayChart()
    {
        if (categoryName != "Category>" && categoryName != "") {


            categoryDisplayed.text = categoryName
            var activityNamesStaticToDisplay: MutableList<String> = mutableListOf()
            var timeActivityNamesStatic: MutableList<Int> = mutableListOf()

            var timeInProcentCategory: Int

            if (categoryName != "Total>") {
                var categoryTime = 1
                var catSomName = ""
                val name = ""

                if (periodToDisplay.text == "Daily>") {
                    GetActivitiesForCharts(categoryName, "test") { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncActivityDaily(categoryName, NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Weekly>") {
                    GetActivitiesForCharts(categoryName, "test") { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncActivityWeekly(categoryName, NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Monthly>") {
                    GetActivitiesForCharts(categoryName, "test") { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncActivityDaily(categoryName, NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Yearly>") {
                    GetActivitiesForCharts(categoryName, "test") { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncActivityYearly(categoryName, NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Total>") {
                    GetActivitiesForCharts(categoryName, "test") { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncActivityTotally(categoryName, NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                }
            } else if (categoryDisplayed.text == "Total>") {

                if (periodToDisplay.text == "Daily>") {
                    GetCategoryCharts { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncCategoryDaily(NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Weekly>") {
                    GetCategoryCharts { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncCategoryWeekly(NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Monthly>") {
                    GetCategoryCharts { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncCategoryMonthly(NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Yearly>") {
                    GetCategoryCharts() { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncCategoryYearly(NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                } else if (periodToDisplay.text == "Total>") {
                    GetCategoryCharts { result ->
                        NamesStaticList = result
                        GlobalScope.launch(Dispatchers.Main) {
                            for (i in NamesStaticList.indices) {
                                val result = withContext(Dispatchers.IO) {
                                    asyncCategoryTotally(NamesStaticList[i])
                                }
                                timeProcent.add(result)
                            }
                            updatePieChart(pieChart)
                        }
                    }
                }
            }
        }

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


    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun showFrequencyDialog(view: View) {
        val options = arrayOf("Daily", "Weekly", "Yearly","Total")
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Choose Frequency")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text = "Daily>"
                    savePeriod("Daily>")
                    this.recreate()

                }

                1 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text = "Weekly>"
                    savePeriod("Weekly>")
                    this.recreate()

                }

                2 -> {
                    periodToDisplay.text = "Yearly>"
                    savePeriod("Yearly>")
                    this.recreate()


                }
                3 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text="Total>"
                    savePeriod("Total>")
                    this.recreate()

                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun ChooseCategoryDisplay(view: View) {
        val options = arrayOf("Total", "Category")
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Category>")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    view.findViewById<TextView>(R.id.selectCategory)?.text = "Total>"
                    saveCategory("Total>")
                    this.recreate()
                }

                1 -> {
                    var intent = Intent(this, Category::class.java)
                    intent.putExtra("period", periodToDisplay.text)
                    startActivity(intent)

                    view.findViewById<TextView>(R.id.selectCategory)?.text = "categoryName"
                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updatePieChart(pieChart: PieChart) {

        val entries = mutableListOf<PieEntry>()
        for(i in NamesStaticList.indices)
        {
            entries.add(PieEntry(timeProcent[i],NamesStaticList[i]))
        }
        val dataSet = PieDataSet(entries, "Activity ")
        dataSet.colors = listOf(
            getColor(R.color.yellow),
            getColor(R.color.blue),
            getColor(R.color.red1),
            getColor(R.color.red2),
            getColor(R.color.red)
        )

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.textSize = 20f // Set the text size for the legend
        legend.textColor = ContextCompat.getColor(this, R.color.black) // Set the text color for the legend

        legend.typeface = Typeface.DEFAULT_BOLD

        legend.formSize=20f

        // Customize the value text color and size
        dataSet.valueTextColor = getColor(R.color.black) // Set this to your desired color for numbers
        dataSet.valueTextSize = 15f

        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD) // Set the value text to be bold


        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }
    private fun getSavedCategory(): String? {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        return sharedPreferences.getString("selectedCategory", null)
    }
    private fun saveCategory(category: String) {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selectedCategory", category)
        editor.apply()
    }

    private fun getSavedPeriod(): String?{
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        return sharedPreferences.getString("selectedPeriod", null)
    }

    private fun savePeriod(period: String) {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selectedPeriod", period)
        editor.apply()
    }


    private suspend fun asyncActivityYearly(catName:String,activityName: String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsActivityYearly("test", catName, activityName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncActivityDaily(catName:String,activityName: String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsActivityDaily("test", catName, activityName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncActivityWeekly(catName:String,activityName: String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsActivityWeekly("test", catName, activityName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }
    private suspend fun asyncActivityTotally(catName:String, activityName: String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsAcrivityTotaly("test", catName, activityName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncActivityMonthly(catName:String,activityName: String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsActivityMonthly("test", catName, activityName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncCategoryDaily(catName:String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsCategoryDaily("test", catName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncCategoryWeekly(catName:String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsCategoryWeekly("test", catName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncCategoryMonthly(catName:String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsCategoryMonthly("test", catName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

    private suspend fun asyncCategoryYearly(catName:String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsCategoryYearly("test", catName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }
    private suspend fun asyncCategoryTotally(catName:String): Float {
        return suspendCancellableCoroutine { continuation ->
            GetStatisticsCategoryTotaly("test", catName) { time ->
                continuation.resume(time * 10f, null)
            }
        }
    }

}
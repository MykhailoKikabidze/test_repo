package Rankings

import ApiRequest.*
import Data.SaveUserEmail
import Data.getSavedUserEmail
import Data.userEmail
import FriendProfile.FriendProfile
import FriendsPage.FriendsPage
import LoginPage.LoginPage
import ProfilePage.ProfilePage
import StatiscticsFunction.asyncActivityDaily
import StatiscticsFunction.asyncActivityYearly
import StatiscticsFunction.asyncCategoryDaily
import StatiscticsFunction.asyncCategoryMonthly
import StatiscticsFunction.asyncCategoryTotally
import StatiscticsFunction.asyncCategoryWeekly
import StatiscticsFunction.asyncCategoryYearly
import StatiscticsFunction.asyncTotallyDaily
import StatiscticsFunction.asyncTotallyTotal
import StatiscticsFunction.asyncTotallyWeekly
import StatiscticsFunction.asyncTotallyYearly
import StatiscticsFunction.updatePieChart
import StatisticCharts.PieChart
import TimerPage.TimerPage
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat.Flags
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.focus_app.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.random.Random
import androidx.core.content.ContextCompat.getColor
import com.github.mikephil.charting.components.LegendEntry
import java.util.Timer


class Rankings : AppCompatActivity(), OnChartValueSelectedListener, NavigationView.OnNavigationItemSelectedListener{
    lateinit var barChart:BarChart

    lateinit var userFriendsList:MutableList<String>
    lateinit var userFriendsListTime:MutableList<Float>
    lateinit var barData: BarData
    lateinit var selectCategory:TextView
    lateinit var periodToDisplay:TextView

    lateinit var barDataSet: BarDataSet

    lateinit var barEntriesList: ArrayList<BarEntry>

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rankings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        selectCategory=findViewById(R.id.selectCategoryRanking)

        periodToDisplay=findViewById(R.id.periodTimeRankings)

        val category=intent.extras?.getString("category")?:"Total"
        val period=intent.extras?.getString("period")?:"Total"
        selectCategory.text=category
        periodToDisplay.text=period

        drawerLayout = findViewById(R.id.toolbar_rank_main)

        val navigationView = findViewById<NavigationView>(R.id.nav_view_rank)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_rank)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        barChart=findViewById(R.id.idBarChart)

        getBarChartData()

        barDataSet=BarDataSet(barEntriesList,"")

        barData=BarData(barDataSet)

        barChart.data=barData

        barDataSet.valueTextColor= Color.BLUE

//        barDataSet.setColors(
//            resources.getColor(R.color.blue)
//        )

        barDataSet.valueTextColor= 25


        barChart.description.isEnabled=false
        barChart.legend.formSize=0f

        barChart.setOnChartValueSelectedListener(this)


    }


    private fun getBarChartData(){
        barEntriesList=ArrayList()
        userFriendsList= mutableListOf<String>()
        userFriendsListTime= mutableListOf<Float>()

        GlobalScope.launch(Dispatchers.Main){
            userFriendsList=withContext(Dispatchers.IO) {
                asyncFriendsList()
            }
            userFriendsList.add(getSavedUserEmail(this@Rankings)?:"")

            if(selectCategory.text=="Total")
            {
                if(periodToDisplay.text=="Daily")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncTotallyDaily(userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Weekly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncTotallyWeekly(userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Monthly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncTotallyWeekly(userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Yearly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncTotallyYearly(userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Total")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncTotallyTotal(userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }

            }
            else
            {
                if(periodToDisplay.text=="Daily")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncCategoryDaily(selectCategory.text.toString(),userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Weekly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncCategoryWeekly(selectCategory.text.toString(),userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Monthly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncCategoryMonthly(selectCategory.text.toString(),userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Yearly")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncCategoryYearly(selectCategory.text.toString(),userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }
                else if(periodToDisplay.text=="Total")
                {
                    for(i in userFriendsList.indices)
                    {
                        val result = withContext(Dispatchers.IO) {
                            asyncCategoryTotally(selectCategory.text.toString(),userFriendsList[i])
                        }
                        userFriendsListTime.add(result)
                    }
                }


            }

            PrepareChart()

        }





//
//
//        barEntriesList.add(BarEntry(1f, 1f))
//        barEntriesList.add(BarEntry(2f, 28f))
//        barEntriesList.add(BarEntry(3f, 3f))
//        barEntriesList.add(BarEntry(4f, 4f))
//        barEntriesList.add(BarEntry(5f, 5f))
    }

    fun showFrequencyDialog(view: View) {
        val options = arrayOf("Daily", "Weekly","Monthly", "Yearly","Total")
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Choose Frequency")
        val intent=Intent(this,Rankings::class.java)
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text = "Daily"
                    intent.putExtra("period","Daily")
                    intent.putExtra("category",selectCategory.text.toString())
                    startActivity(intent)

                }

                1 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text = "Weekly"
                    intent.putExtra("period","Weekly")
                    intent.putExtra("category",selectCategory.text.toString())
                    startActivity(intent)


                }

                2 -> {
                    periodToDisplay.text = "Monthly"
                    intent.putExtra("period","Monthly")
                    intent.putExtra("category",selectCategory.text.toString())
                    startActivity(intent)



                }
                3 -> {
                    view.findViewById<TextView>(R.id.periodTime)?.text="Yearly"
                    intent.putExtra("period","Yearly")
                    intent.putExtra("category",selectCategory.text.toString())
                    startActivity(intent)


                }
                4->{
                    view.findViewById<TextView>(R.id.periodTime)?.text="Total"
                    intent.putExtra("period","Total")
                    intent.putExtra("category",selectCategory.text.toString())
                    startActivity(intent)
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
        builder.setTitle("Select category")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    view.findViewById<TextView>(R.id.selectCategoryRanking)?.text = "Total>"
                    this.recreate()
                }

                1 -> {
                    val intent = Intent(this, CategoryRanking::class.java)
                    startActivity(intent)

                    view.findViewById<TextView>(R.id.selectCategoryRanking)?.text = "categoryName"
                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun PrepareChart()
    {

        for(i in userFriendsList.indices)
        {
            barEntriesList.add(BarEntry(1f+i.toFloat(),userFriendsListTime[i],userFriendsList))
        }




        barDataSet.setColors(
            getColor(this,R.color.red1),
            getColor(this,R.color.red2),
            getColor(this,R.color.blue),
            getColor(this,R.color.red),
            getColor(this,R.color.red),
            getColor(this,R.color.yellow)
        )


        val colors = listOf(
            ContextCompat.getColor(this, R.color.red1),
            ContextCompat.getColor(this, R.color.red1),
            ContextCompat.getColor(this, R.color.red1),
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.yellow)
        )


        barDataSet = BarDataSet(barEntriesList, "")

        barData = BarData(barDataSet)
        barDataSet.colors = colors

        val legend = barChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textColor = ContextCompat.getColor(this, R.color.black)
        legend.formSize=20f

        legend.typeface = Typeface.DEFAULT_BOLD

        val userLoginList= mutableListOf<String>()
        GlobalScope.launch(Dispatchers.Main) {
            for (i in userFriendsList.indices) {
                val user = withContext(Dispatchers.IO) {
                    asyncFriendName(userFriendsList[i])
                }
                userLoginList.add(user)

            }
            UpdateChart(legend,userLoginList,colors)

        }

    }


    fun UpdateChart(legend:Legend,userLoginList:MutableList<String>,colors: List<Int>)
    {

        val legendEntries=legendLogins(userLoginList,colors)

        legend.setCustom(legendEntries)


        barData.setValueTextSize(20f)
        barData.setValueTypeface(Typeface.DEFAULT_BOLD)
        barChart.data = barData

        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.xAxis.isEnabled = false
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        barChart.invalidate()
    }

    fun getRandomColor(): Int {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        return Color.rgb(red, green, blue)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.statistics_menu -> {
                val intent = Intent(this, PieChart::class.java)
                startActivity(intent)
                return true
            }

            R.id.friends -> {
                val intent = Intent(this, FriendsPage::class.java)
                startActivity(intent)
                return true
            }

            R.id.rankings ->
            {
                val intent= Intent(this,Rankings::class.java)
                startActivity(intent)
            }

            R.id.timer->{
                val intent= Intent(this, TimerPage::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout->{
                SaveUserEmail(this,null)
                intent= Intent(this, LoginPage::class.java)
                startActivity(intent)
            }

            R.id.profile -> {
                val intent= Intent(this, ProfilePage::class.java)
                startActivity(intent)
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

    suspend fun asyncFriendName(friendEmeil:String):String {
        return suspendCancellableCoroutine { continuation ->
            GetUserLogin(friendEmeil){result->
                continuation.resume(result, null)
            }
        }
    }

    fun legendLogins(userLoginList: MutableList<String>,colors:List<Int>):List<LegendEntry>
    {
        val legendEntries = userLoginList.mapIndexed { index, name ->
            LegendEntry().apply {
                this.label = name
                this.formColor = colors.getOrNull(index) ?: Color.BLACK
            }
        }
        return legendEntries
    }

    suspend fun asyncFriendsList(): MutableList<String> {
        return suspendCancellableCoroutine { continuation ->
            val user= getSavedUserEmail(this)?:""
            GetFriends(user){result->
                continuation.resume(result.toMutableList(), null)
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        e?.let {
            val position = it.x.toInt()
            // Toast.makeText(this, userFriendsList[position], Toast.LENGTH_SHORT).show()
        }    }

    override fun onNothingSelected() {
        // Handle case when nothing is selected if needed
    }
}
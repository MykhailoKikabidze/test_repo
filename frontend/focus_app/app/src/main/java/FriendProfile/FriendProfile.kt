package FriendProfile

import ApiRequest.GetActivitiesForCharts
import ApiRequest.GetCategoryCharts
import ProfilePage.ProfilePage
import StatiscticsFunction.asyncActivityDaily
import StatiscticsFunction.asyncActivityMonthly
import StatiscticsFunction.asyncActivityTotally
import StatiscticsFunction.asyncActivityWeekly
import StatiscticsFunction.asyncActivityYearly
import StatiscticsFunction.asyncCategoryDaily
import StatiscticsFunction.asyncCategoryMonthly
import StatiscticsFunction.asyncCategoryTotally
import StatiscticsFunction.asyncCategoryWeekly
import StatiscticsFunction.asyncCategoryYearly
import StatiscticsFunction.updatePieChart
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.focus_app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import StatiscticsFunction.*
import android.app.AlertDialog
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.navigation.NavigationView

class FriendProfile : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var pieChart: PieChart
    private lateinit var savedPeriod: String
    private lateinit var NamesStaticList: MutableList<String>
    private lateinit var categoryName: String
    var timeProcent: MutableList<Float> = mutableListOf()
    private lateinit var periodToDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friend_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar_friend_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        periodToDisplay=findViewById(R.id.periodTimeFriend)

        pieChart=findViewById(R.id.pie_chart_friend)
        periodToDisplay.text=intent.extras?.getString("period")?:"Daily>"

        categoryName = intent.extras?.getString("categoryName") ?: "hobby"


        DisplayChart()


        drawerLayout = findViewById(R.id.toolbar_friend_main)

        val navigationView = findViewById<NavigationView>(R.id.nav_view_friend)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_friend)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }


    fun DisplayChart() {
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
                    updatePieChart(
                        "Categories",
                        this@FriendProfile,
                        pieChart,
                        timeProcent,
                        NamesStaticList
                    )
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
                    updatePieChart(
                        "Categories",
                        this@FriendProfile,
                        pieChart,
                        timeProcent,
                        NamesStaticList
                    )
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
                    updatePieChart(
                        "Categories",
                        this@FriendProfile,
                        pieChart,
                        timeProcent,
                        NamesStaticList
                    )
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
                    updatePieChart(
                        "Categories",
                        this@FriendProfile,
                        pieChart,
                        timeProcent,
                        NamesStaticList
                    )
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
                    updatePieChart(
                        "Categories",
                        this@FriendProfile,
                        pieChart,
                        timeProcent,
                        NamesStaticList
                    )
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
                val intent= Intent(this, StatisticCharts.PieChart::class.java)
                startActivity(intent)
                return true
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
    fun showFrequencyDialog(view: View) {
        val options = arrayOf("Daily", "Weekly", "Yearly","Total")
        val builder = AlertDialog.Builder(view.context)
        val intent=Intent(this,FriendProfile::class.java)
        builder.setTitle("Choose Frequency")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    view.findViewById<TextView>(R.id.periodTimeFriend)?.text = "Daily>"
                    intent.putExtra("period","Daily>")
                }

                1 -> {
                    view.findViewById<TextView>(R.id.periodTimeFriend)?.text = "Weekly>"
                    intent.putExtra("period","Weekly>")
                }

                2 -> {
                    periodToDisplay.text = "Yearly>"
                    intent.putExtra("period","Yearly>")
                }
                3 -> {
                    view.findViewById<TextView>(R.id.periodTimeFriend)?.text="Total>"
                    intent.putExtra("period","Total>")
                }
            }
            startActivity(intent)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
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
}
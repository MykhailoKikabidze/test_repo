package TimerPage

import ApiRequest.CreateActivityLog
import ApiRequest.GetPoints
import ApiRequest.UpdatePoints
import Data.SaveUserEmail
import Data.userEmail
import FriendProfile.FriendProfile
import LoginPage.LoginPage
import ProfilePage.ProfilePage
import StatisticCharts.PieChart
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.focus_app.R
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Date
private var isTimer=true
class TimerPage : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var minutes: TextView
    private lateinit var seconds: TextView
    private lateinit var startFocus: Button
    private lateinit var activityName:String
    private lateinit var categoryName:String


    private var isRunning=false
    private var counterOfSeconds = 0
    private var counterOfMinutes=0
    private var timeSpend=25

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_timer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val changeTimerPage=findViewById<ImageView>(R.id.changeTimer)
        seconds = findViewById(R.id.seconds) //to count seconds
        minutes=findViewById(R.id.minutes) //to count minutes
        startFocus=findViewById(R.id.StartFocusBtn) //Start Focus button
        val selectCategoryView: TextView =findViewById(R.id.select_Tag)
        val userPoints:TextView=findViewById(R.id.points_timer)

        activityName=intent.extras?.getString("activity")?:""

        drawerLayout=findViewById(R.id.toolbar_timer_main)

        val navigationView=findViewById<NavigationView>(R.id.nav_view_timer)
        navigationView.setNavigationItemSelectedListener(this)

        toolbar = findViewById(R.id.toolbar_timer)
        val toggle= ActionBarDrawerToggle(this,drawerLayout, toolbar ,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


      //  if(userEmail!="") {
            GetPoints("test" ,{ result ->
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            },
            { points->
                userPoints.text="${points}"
            })
       // }
        categoryName=intent.extras?.getString("category")?:""
        if(categoryName !="")
        {
            selectCategoryView.text= categoryName
        }
        else
        {
            Toast.makeText(this, "choose category", Toast.LENGTH_SHORT).show()
        }

        startFocus.setOnClickListener{
            updateButtonText()
            InitializeTimer()
        }
        InitializeTimer()



        selectCategoryView.setOnClickListener {
            replaceFragment(CategoryView())
        }

            changeTimerPage.setOnClickListener() {
                isTimer = !isTimer
                if(isTimer)
                {
                    timeSpend=25
                }
                else {
                    timeSpend = 0
                }
                counterOfSeconds=0
                InitializeTimer()
            }

    }

    private fun InitializeTimer() {
        if(activityName!="" ) //if activity name is not empty then timer will start
        {
//            if(categoryName!="")//if category name is not empty then timer will start
//            {
                if (timeSpend == 0)
                    minutes.text = "00"
                else
                    minutes.text = timeSpend.toString()
                counterOfMinutes = timeSpend
                counterOfSeconds = 0
                seconds.text = "00"
                updateButtonText()
                val handler = Handler(Looper.getMainLooper())

                val runnable = object : Runnable {

                    override fun run() {


                        if (isTimer) {
                            if (counterOfMinutes == 0 && counterOfSeconds == 0) {// Timer has reached 00:00 and it stops the timer
                                isRunning = false
                                updateButtonText()
                                handler.removeCallbacks(this)
                                val currentDate = Date()
                                val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                                val date = dateFormat.format(currentDate)
                                val timeSpented = counterOfMinutes * 60 + counterOfSeconds
                                val pointsForFocus = timeSpend / 60
                                val request = mapOf(
                                    "date" to "$date",
                                    "time_spent" to timeSpented
                                )
                                UpdatePoints("test", pointsForFocus, "add") { result ->
                                    Toast.makeText(this@TimerPage, result, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                //add activity that user made with timer

                                CreateActivityLog(
                                    request,
                                    categoryName,
                                    userEmail,
                                    activityName
                                ) { result ->
                                    Toast.makeText(this@TimerPage, result, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                return
                            }

                            if (counterOfSeconds == 0) {
                                counterOfMinutes--
                                counterOfSeconds = 59
                            } else
                                counterOfSeconds--

                            if (counterOfMinutes < 10)
                                minutes.text =
                                    "0$counterOfMinutes"//to make it look like 01 in minutes
                            else
                                minutes.text = "$counterOfMinutes"

                            if (counterOfSeconds < 10)
                                seconds.text =
                                    "0$counterOfSeconds" //to make it look like 01 in seconds
                            else
                                seconds.text = "$counterOfSeconds"
                            handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second
                        } else {
                            if (!isRunning) {// Timer stops and it stops the timer
                                //isRunning = false
                                isRunning = !isRunning
                                minutes.text = "00"
                                seconds.text = "00"
                                handler.removeCallbacks(this)
                                val currentDate = Date()
                                val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                                val date = dateFormat.format(currentDate)
                                var timeSpented = counterOfMinutes * 60 + counterOfSeconds
                                val pointsForFocus = timeSpend / 60

                                val request = mapOf(
                                    "date" to "$date",
                                    "time_spent" to timeSpented
                                )

                                UpdatePoints("test", pointsForFocus, "add") { result ->
                                    Toast.makeText(this@TimerPage, result, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                //send created activity log to the backend
                                CreateActivityLog(
                                    request,
                                    categoryName,
                                    userEmail,
                                    activityName
                                ) { result ->
                                    Toast.makeText(this@TimerPage, result, Toast.LENGTH_SHORT)
                                        .show()
                                }
                                counterOfSeconds = 0
                                counterOfMinutes = 0
                                return
                            }

                            if (counterOfSeconds == 60) {
                                counterOfMinutes++
                                counterOfSeconds = 0
                            } else
                                counterOfSeconds++

                            if (counterOfMinutes < 10)
                                minutes.text =
                                    "0$counterOfMinutes"//to make it look like 01 in minutes
                            else
                                minutes.text = "$counterOfMinutes"

                            if (counterOfSeconds < 10)
                                seconds.text =
                                    "0$counterOfSeconds" //to make it look like 01 in seconds
                            else
                                seconds.text = "$counterOfSeconds"
                            handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second
                        }

                    }


                }



                startFocus.setOnClickListener() //what will be done when we click on button Start Focus
                {

                    if (activityName == "") {
                        Toast.makeText(this, "You haven't choose activity!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        isRunning = !isRunning
                        updateButtonText() //update button to Stop Focus
                        if (isRunning) {
                            handler.post(runnable)
                        } else
                            handler.removeCallbacks(runnable)
                    }
                }
            }
//            else
//            {
//                Toast.makeText(this@TimerPage, "choose activity", Toast.LENGTH_SHORT).show()
//            }
//        }
        else{
            Toast.makeText(this@TimerPage, "choose activity", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateButtonText()
    {
        val buttonText=if(isRunning)"Stop Focus" else "Start Focus"
        findViewById<Button>(R.id.StartFocusBtn)?.text=buttonText
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.statistics_menu -> {
                val intent=Intent(this,PieChart::class.java)
                startActivity(intent)
                return true
            }

            R.id.friends->{
                val intent=Intent(this,FriendProfile::class.java)
                startActivity(intent)
                return true
            }

            R.id.timer->{
                val intent=Intent(this,TimerPage::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout->{
                SaveUserEmail(this,null)
                intent=Intent(this, LoginPage::class.java)
                startActivity(intent)
            }

            R.id.profile -> {
                val intent=Intent(this,ProfilePage::class.java)
                startActivity(intent)
                return true
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction: FragmentTransaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_timer,fragment)
        transaction.commit()
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

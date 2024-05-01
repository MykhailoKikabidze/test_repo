package TimerPage

import ApiRequest.CreateActivityLog
import ApiRequest.UpdatePoints
import Data.activityName
import Data.categoryName
import Data.userEmail
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.focus_app.R
import java.text.SimpleDateFormat
import java.util.Date

class TimerPageStopWatch : AppCompatActivity() {
    private var isRunning=false
    private var counterOfSeconds = 0
    private var counterOfMinutes=0
    private var timeSpend=0

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
        val seconds: TextView = findViewById(R.id.seconds) //to count seconds
        val minutes: TextView =findViewById(R.id.minutes) //to count minutes
        val startFocus: Button =findViewById(R.id.StartFocusBtn) //Start Focus button
        val selectCategoryView: TextView =findViewById(R.id.select_Tag)
        if(categoryName!="")
        {
            selectCategoryView.text=categoryName;
        }

        selectCategoryView.setOnClickListener {
            replaceFragment(CategoryView())
        }

        val handler = Handler(Looper.getMainLooper())

        updateButtonText()
        val runnable = object : Runnable {

            override fun run() {
                if (!isRunning) {// Timer stops and it stops the timer
                    //isRunning = false
                    isRunning=!isRunning
                    minutes.text="00"
                    seconds.text="00"
                    handler.removeCallbacks(this)
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                    val date=dateFormat.format(currentDate)
                    var timeSpented =counterOfMinutes*60+counterOfSeconds
                    val pointsForFocus=timeSpend/60

                    val request = mapOf(
                        "date" to "$date",
                        "time_spent" to timeSpented
                    )

                    UpdatePoints("test",pointsForFocus,"add"){result->
                        Toast.makeText(this@TimerPageStopWatch, result, Toast.LENGTH_SHORT).show()
                    }
                    //send created activity log to the backend
                    CreateActivityLog(request, categoryName, userEmail, categoryName){ result->
                        Toast.makeText(this@TimerPageStopWatch, result, Toast.LENGTH_SHORT).show()
                    }
                    counterOfSeconds=0
                    counterOfMinutes=0
                    return
                }

                if(counterOfSeconds==60)
                {
                    counterOfMinutes++
                    counterOfSeconds=0
                }
                else
                    counterOfSeconds++

                if(counterOfMinutes<10)
                    minutes.text="0$counterOfMinutes"//to make it look like 01 in minutes
                else
                    minutes.text="$counterOfMinutes"

                if(counterOfSeconds<10)
                    seconds.text="0$counterOfSeconds" //to make it look like 01 in seconds
                else
                    seconds.text="$counterOfSeconds"
                handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second
            }
        }
        changeTimerPage.setOnClickListener(){
            val intent= Intent(this,TimerPage::class.java)
            startActivity(intent)
        }




        startFocus.setOnClickListener() //what will be done when we click on button Start Focus
        {

//            if(activityName=="")
//            {
//                Toast.makeText(this, "You haven't choose activity!", Toast.LENGTH_SHORT).show()
//            }
//            else
//            {
                isRunning=!isRunning
                updateButtonText() //update button to Stop Focus
                if(isRunning){
                    handler.post(runnable)
                }
//            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction: FragmentTransaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_timer,fragment)
        transaction.commit()
    }
    private fun updateButtonText()
    {
        val buttonText=if(isRunning)"Stop Focus" else "Start Focus"
        findViewById<Button>(R.id.StartFocusBtn)?.text=buttonText
    }
}

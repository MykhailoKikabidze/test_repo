package TimerPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.focus_app.R

class TimerPage : AppCompatActivity() {
    private var isRunning=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_timer_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_timer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val seconds: TextView = findViewById(R.id.seconds) //to count seconds
        val minutes: TextView =findViewById(R.id.minutes) //to count minutes
        val startFocus: Button =findViewById(R.id.StartFocusBtn) //Start Focus button
        val selectCategoryView: TextView =findViewById(R.id.select_Tag)


        selectCategoryView.setOnClickListener {
            intent= Intent(this,CategoryView::class.java)
            startActivity(intent)
        }

        val handler = Handler(Looper.getMainLooper())

        updateButtonText()
        val runnable = object : Runnable {
            var counterOfSeconds = 0
            var counterOfMinutes=0
            override fun run() {
                counterOfSeconds++
                if(counterOfSeconds<10)
                    seconds.text="0$counterOfSeconds" //to make it look like 01
                else
                    seconds.text="$counterOfSeconds"
                // Your timer logic here
                if(counterOfSeconds==60)
                {
                    counterOfMinutes++
                    counterOfSeconds=0
                    if(counterOfMinutes<10)
                        minutes.text="0$counterOfMinutes"//to make it look like 01
                    else
                        minutes.text="$counterOfMinutes"
                }
                handler.postDelayed(this, 1000) // 1000 milliseconds = 1 second
            }
        }


        startFocus.setOnClickListener() //what will be done when we click on button Start Focus
        {
            updateButtonText() //update button to Stop Focus
            isRunning=!isRunning
            if(isRunning)
                handler.removeCallbacks(runnable)
            else
                handler.post(runnable)

        }
    }

    private fun updateButtonText()
    {
        val buttonText=if(isRunning)"Stop Focus" else "Start Focus"
        findViewById<Button>(R.id.StartFocusBtn)?.text=buttonText
    }
}
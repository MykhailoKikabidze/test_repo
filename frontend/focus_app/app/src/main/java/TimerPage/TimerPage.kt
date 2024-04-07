package TimerPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.focus_app.R

class TimerPage : Fragment() {
    private var isRunning=true

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflater=LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.fragment_timer_page, container, false) //this interface is from fragment_home
        val seconds: TextView = view.findViewById(R.id.seconds) //to count seconds
        val minutes: TextView =view.findViewById(R.id.minutes) //to count minutes
        val startFocus: Button =view.findViewById(R.id.StartFocusBtn) //Start Focus button
        val selectCategoryView: TextView =view.findViewById(R.id.select_Tag)


        selectCategoryView.setOnClickListener {
            val newLayout = inflater.inflate(R.layout.category_view_timer, container, false)
            container?.addView(newLayout)
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
            Toast.makeText(requireContext(), "WOW", Toast.LENGTH_SHORT).show()
            // Start the timer initially
            if(isRunning)
                handler.removeCallbacks(runnable)
            else
                handler.post(runnable)

        }
        return view
    }

    private fun updateButtonText()
    {
        val buttonText=if(isRunning)"Stop Focus" else "Start Focus"
        view?.findViewById<Button>(R.id.StartFocusBtn)?.text=buttonText
    }
}
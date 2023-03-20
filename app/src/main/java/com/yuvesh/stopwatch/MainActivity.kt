package com.yuvesh.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yuvesh.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
     private var isstarted=false
    lateinit var serviceIntent: Intent
    var time=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener{
                      startorstop()
        }
        binding.btnReset.setOnClickListener {
            reset()
        }
        serviceIntent=Intent(applicationContext,StopwatchService::class.java)
        registerReceiver(updatedTime, IntentFilter(StopwatchService.updatedtime))
    }

    private fun reset() {

         stop()
        time=0.0
        binding.txttime.text=getFormattedTime(time)
    }

    private fun startorstop() {
        if(isstarted)
        {
            stop()
        }
        else
            start()
    }

    private fun stop() {

       stopService(serviceIntent)
        binding.btnReset.text="Start"
        isstarted=false
    }
    private fun start()
    {
        serviceIntent.putExtra(StopwatchService.currenttime,time)
        startService(serviceIntent)
    binding.btnStart.text="Stop"
        isstarted=true
    }

    private val updatedTime:BroadcastReceiver=object:BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent) {
                 time=intent.getDoubleExtra(StopwatchService.currenttime,0.0)
                 binding.txttime.text=getFormattedTime(time)

        }

    }
    private fun getFormattedTime(time:Double):String{

             val timeInt=time.roundToInt()
        val hours=timeInt%86400/3600
        val minutes=timeInt%86400%3600/60
        val seconds= timeInt%86400%3600%60
        return String.format("%02d:%02d:%02d",hours,minutes,seconds)
    }

}
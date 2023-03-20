package com.yuvesh.stopwatch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask

class StopwatchService:Service() {
    private val timer=Timer()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time=intent.getDoubleExtra(currenttime,0.0)
        timer.scheduleAtFixedRate(StopWatchTask(time),0,1000)
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    companion object{
        const val currenttime="Current time"
        const val updatedtime="Updated time"
    }


    private inner class StopWatchTask(private var time:Double):TimerTask()
    {
        override fun run() {

            val intent=Intent(updatedtime)
            time++

            intent.putExtra(currenttime,time)
            sendBroadcast(intent)
        }

    }
}
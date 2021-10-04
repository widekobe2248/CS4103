package com.example.hw4_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.system.exitProcess

class StartScreen : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        var startBtn = findViewById<Button>(R.id.startBtn)
        var quitBtn = findViewById<Button>(R.id.quitBtn)
        var titleText = findViewById<TextView>(R.id.titleText)

        startBtn.setOnClickListener(this)
        quitBtn.setOnClickListener(this)


    }

    override fun onClick(v: View) {

        if(v.id == R.id.startBtn)
        {
            startNextActivity(v)
        }
        if(v.id == R.id.quitBtn)
        {
            exitProcess(0)
        }
    }

    private fun startNextActivity(view: View) {
        val intent = Intent(this, GameSettings::class.java)
        startActivity(intent)
    }

}
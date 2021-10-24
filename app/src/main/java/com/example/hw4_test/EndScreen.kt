package com.example.hw4_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class EndScreen : AppCompatActivity(),  View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_screen)

        val bundle: Bundle? = intent.extras
        val winner_num = bundle?.get("player_winner")

        val winner_text = "Player $winner_num Won the Game!"

        val winner_textField = findViewById<TextView>(R.id.winnerText)

        winner_textField.setText(winner_text)

        var playAgainBtn = findViewById<Button>(R.id.backToMenu)

        playAgainBtn.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if(v.id == R.id.backToMenu)
        {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent)
        }
    }
}
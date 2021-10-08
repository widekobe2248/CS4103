package com.example.hw4_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class GameSettings : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    var player_amount = 1
    var connections_win_amt = 4
    var board_width = 9
    var board_height = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        var start_game = findViewById<Button>(R.id.start_game_btn)


        //Handles Drop-Down Menu for player amount
        var player_options: Spinner = findViewById(R.id.player_amt)
        ArrayAdapter.createFromResource(this, R.array.player_amount, android.R.layout.simple_spinner_dropdown_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            player_options.adapter = adapter
        }
        player_options.onItemSelectedListener = this

        var connection_win: Spinner = findViewById(R.id.connection_win_amt)
        ArrayAdapter.createFromResource(this, R.array.connection_win_amount, android.R.layout.simple_spinner_dropdown_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            player_options.adapter = adapter
        }
        connection_win.onItemSelectedListener = this


        start_game.setOnClickListener(this)
    }

    override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
        //Has to be converted to string before casting to Int
        //This is because it returns type Any! which you cannot cast to
        //Int but you can Cast to String then to Int
        if (v != null) {
            if(v.id == R.id.player_amt)
            {
                var selected = parent.getItemAtPosition(pos) as String
                player_amount = selected.toInt()
            }
            if(v.id == R.id.connection_win_amt)
            {
                var selected = parent.getItemAtPosition(pos) as String
                connections_win_amt = selected.toInt()

            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Nothing Needed Just a required method for AdapterView.OnItemSelectedListener
    }

    override fun onClick(v: View) {
        if(v.id == R.id.start_game_btn)
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("player_amt", player_amount)
            intent.putExtra("connection_win", connections_win_amt )
            intent.putExtra("board_width", board_width)
            intent.putExtra("board_height", board_height)
            startActivity(intent)
        }

    }
}
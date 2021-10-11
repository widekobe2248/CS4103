package com.example.hw4_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class GameSettings : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    var player_amount = 1
    var connections_win_amt = 4
    var board_width = 9
    var board_height = 5



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        var start_game = findViewById<Button>(R.id.start_game_btn)





        var player_options = findViewById<Spinner>(R.id.player_amt)
        var adapter = ArrayAdapter.createFromResource(this, R.array.player_amount, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        player_options.adapter = adapter
        player_options.onItemSelectedListener = this



        var connections_win = findViewById<Spinner>(R.id.connection_win_amt)
        var adapter1 = ArrayAdapter.createFromResource(this, R.array.connection_win_amount, android.R.layout.simple_spinner_dropdown_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        connections_win.adapter = adapter1
        connections_win.onItemSelectedListener = this


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
            var widthBoardSizeInput = findViewById<EditText>(R.id.widthText)
            var lengthBoardSizeInput = findViewById<EditText>(R.id.lengthText)
            board_width = widthBoardSizeInput.text.toString().toInt()
            board_height = lengthBoardSizeInput.text.toString().toInt()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("player_amt", player_amount)
            intent.putExtra("connection_win", connections_win_amt )
            intent.putExtra("board_width", board_width)
            intent.putExtra("board_height", board_height)
            startActivity(intent)
        }

    }
}
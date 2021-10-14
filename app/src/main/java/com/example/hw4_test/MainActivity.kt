package com.example.hw4_test

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class Board(val gridLayout: GridLayout, val context: Context) {
    val board : MutableList<MutableList<BoardItem>> = ArrayList()
    var playerTurn : Int = 1
    var numPlayers = 2

    constructor(gridLayout: GridLayout, context: Context, numPlayers: Int) : this(gridLayout, context) {
        this.numPlayers = numPlayers
    }

    init {
        for (i in 0 until this.gridLayout.rowCount) {
            this.board.add(mutableListOf<BoardItem>())
            for (j in 0 until this.gridLayout.columnCount) {
                val tv = TextView(context)

                tv.setBackgroundResource(R.drawable.ic_white_circle)

                val param = GridLayout.LayoutParams()

                param.columnSpec = GridLayout.spec(j, 1f)
                param.rowSpec = GridLayout.spec(i, 1f)

                tv.layoutParams = param

                this.addItem(i, tv)
            }
        }
    }

    private fun addItem(index: Int, textView: TextView) {
        this.board[index].add(BoardItem(textView))
        this.gridLayout.addView(textView)
    }

    fun move(column: Int, piece: Int = this.playerTurn): Int {
        var testRow = this.gridLayout.rowCount - 1

        // get the lowest empty tile or 0
        while(this.board[testRow][column].state != 0 && testRow > 0 ) {
            testRow--
        }

        if (this.board[testRow][column].fillSpot(piece)) {
            this.playerTurn = getNextPlayer(piece)
        }

        return this.playerTurn
    }

    fun clearBoard() {
        for (i in 0 until this.gridLayout.rowCount) {
            for (j in 0 until this.gridLayout.columnCount) {
                this.board[i][j].overrideSpot(0)
            }
        }
    }

    fun getNextPlayer(currentPlayer: Int = this.playerTurn): Int {
        return (currentPlayer ) % this.numPlayers + 1
    }

}

class BoardItem(val tv: TextView) {
    var state = 0

    // sets the spot's state, if currently empty
    fun fillSpot(piece: Int):Boolean {

        // if spot is taken, do nothing
        if (this.state != 0) return false

        overrideSpot(piece)
        return true
    }

    // sets the spot's state, regardless of current state
    fun overrideSpot(piece: Int) {
        when (piece) {
            1 -> {
                this.tv.setBackgroundResource(R.drawable.ic_red_circle)
                this.state = piece
            }
            2 -> {
                this.tv.setBackgroundResource(R.drawable.ic_yellow_circle)
                this.state = piece
            }
            3 -> {
                this.tv.setBackgroundResource(R.drawable.ic_green_circle)
                this.state = piece
            }
            else -> {
                this.tv.setBackgroundResource(R.drawable.ic_white_circle)
                this.state = 0

            }
        }
    }
}

class Slider(val board: Board, val seekBar: SeekBar) {

    var acceleration : Float = 0f
    var velocity : Float = 0f
    val scalar: Int = 1000
    var columnPosition: Int = 0
        get() {
            return (seekBar.progress / scalar.toFloat()).roundToInt()
        }

    init {

        seekBar.max = (board.gridLayout.columnCount - 1) * scalar
        seekBar.progress = 0

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, value: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {
            }

            override fun onStopTrackingTouch(seek: SeekBar?) {
            }
        })

        val mOrientationListener = object : OrientationEventListener(
            board.context,
            SensorManager.SENSOR_DELAY_NORMAL
        ) {
            override fun onOrientationChanged(orientation: Int) {
                val scaledOrientation = (orientation + 180) % 360
                val mappedOrientation: Float = mapProgress(scaledOrientation,135, 225, -0.02f, 0.02f)

                acceleration = mappedOrientation
            }
        }

        if (mOrientationListener.canDetectOrientation()) {
            Log.v("dbg", "Can detect orientation")
            mOrientationListener.enable()
        } else {
            Log.v("dbg", "Cannot detect orientation")
            mOrientationListener.disable()
        }


        val timer = Timer()
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {

                    if (seekBar.progress <= seekBar.max && seekBar.progress >= 0) {
                        velocity += acceleration
                    }

                    // add some friction to the slider
                    velocity *= 0.9995f

                    seekBar.progress += velocity.roundToInt()

                }
            },
            0, 2
        )

    }

    fun mapProgress(x: Int, i_min: Int, i_max: Int, o_min: Float, o_max: Float): Float {
        return (x - i_min) * (o_max - o_min) / (i_max - i_min) + o_min
    }

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = 9
        gridLayout.rowCount = 6

        val layoutParams = gridLayout.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = "${gridLayout.columnCount}:${gridLayout.rowCount}"
        gridLayout.layoutParams = layoutParams

        val players = mapOf(1 to R.color.red, 2 to R.color.yellow)
//        val players = mapOf(1 to R.color.red, 2 to R.color.yellow, 3 to R.color.green)

        val board = Board(gridLayout,this, players.size)

        val slider = Slider(board, seekBar)

        // nextPlayer
        val turn = findViewById<Button>(R.id.turn)
        turn.setBackgroundColor(ContextCompat.getColor(this, players[1] ?: R.color.red))
        turn?.setOnClickListener {
            val nextPlayer: Int = board.move(slider.columnPosition)
            turn.setBackgroundColor(ContextCompat.getColor(this, players[nextPlayer] ?: R.color.red))
        }

        // clear
        val clearButton = findViewById<Button>(R.id.clear)
        clearButton?.setOnClickListener {
             board.clearBoard()

        }
    }
}
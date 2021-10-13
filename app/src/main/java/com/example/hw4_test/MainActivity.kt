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
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import java.util.*
import kotlin.collections.ArrayList


class Board(val gridLayout: GridLayout, val context: Context) {
    val board : MutableList<MutableList<BoardItem>> = ArrayList()
    var playerTurn : Int = 1
    var numPlayers = 2

    constructor(gridLayout: GridLayout, context: Context, numPlayers: Int) : this(gridLayout, context) {
        this.numPlayers = numPlayers
    }

    init {

        // calculate the desired width and height
        val w : Int = this.gridLayout.layoutParams.width / this.gridLayout.columnCount
//        val w : Int = this.gridLayout.measuredWidth / this.gridLayout.columnCount

        // this uses width because height is not defined in the layout, and it's a square
        val h : Int = this.gridLayout.layoutParams.width / this.gridLayout.rowCount
//        val h : Int = this.gridLayout.measuredWidth / this.gridLayout.rowCount


        // we want the objects to fit on the screen, so take the smaller dimension
        val dimen : Int = if (w < h) w else h

        Log.d("dimen", "w: ${w}, h: ${h}, dimen: ${dimen}")


        for (i in 0 until this.gridLayout.rowCount) {
            this.board.add(mutableListOf<BoardItem>())
            for (j in 0 until this.gridLayout.columnCount) {
                val tv = TextView(context)

                tv.setBackgroundResource(R.drawable.ic_white_circle)

                val param = GridLayout.LayoutParams()

                // set width and height
                param.width = dimen
                param.height = dimen

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

    var tilt : Float = 0f
    var distance : Float = 0f
    var columnPosition: Int = 0
        get() {
            return seekBar.progress / 100
        }

    init {

        seekBar.max = (board.gridLayout.columnCount - 1) * 100
        seekBar.progress = 0

        seekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, value: Int, fromUser: Boolean) {
//                j = (value / 100)

//                textView.text = j.toString()
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {
            }

            override fun onStopTrackingTouch(seek: SeekBar?) {
            }

        })

        var mOrientationListener = object : OrientationEventListener(
            board.context,
            SensorManager.SENSOR_DELAY_NORMAL
        ) {
            override fun onOrientationChanged(orientation: Int) {
                val scaledOrientation = (orientation + 180) % 360
                val mappedOrientation: Float = mapProgress(scaledOrientation,135, 225, -2f, 2f)

                tilt = mappedOrientation
            }
        }

        if (mOrientationListener.canDetectOrientation() === true) {
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
                    distance += if (tilt > seekBar.max) 0f else tilt
                    seekBar.progress = distance.toInt()
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
        val textView = findViewById<TextView>(R.id.textView)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = 9
        gridLayout.rowCount = 6

        val players = mapOf(1 to R.color.red, 2 to R.color.yellow)
//        val players = mapOf(1 to R.color.red, 2 to R.color.yellow, 3 to R.color.green)

        val board = Board(gridLayout,this, players.size)

//        var j = 0


        val slider = Slider(board, seekBar)



        // red
        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener {
            board.move(slider.columnPosition, 1)
        }

        // yellow
        val button2 = findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            board.move(slider.columnPosition, 2)
        }

        // nextPlayer
        val turn = findViewById<Button>(R.id.turn)
        turn.setBackgroundColor(ContextCompat.getColor(this, players[1] ?: R.color.red))
        turn?.setOnClickListener {
            val nextPlayer: Int = board.move(slider.columnPosition)
            turn.setBackgroundColor(ContextCompat.getColor(this, players[nextPlayer] ?: R.color.red))
        }

        // right
//        val button3 = findViewById<Button>(R.id.button3)
//        button3?.setOnClickListener {
//
//            j = if (j < board.gridLayout.columnCount-1) j+1 else 0
//
//            seekBar.progress = j
//
//        }

        // left
//        val button4 = findViewById<Button>(R.id.button4)
//        button4?.setOnClickListener {
//
//            j = if (j > 0 ) j-1 else board.gridLayout.columnCount - 1
//
//            seekBar.progress = j
//
//        }

        // clear
        val clearButton = findViewById<Button>(R.id.clear)
        clearButton?.setOnClickListener {
             board.clearBoard()

        }


    }


}
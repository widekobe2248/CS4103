package com.example.hw4_test

import android.content.Context
import android.content.Intent
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
    var connectionAmount = 4

    constructor(gridLayout: GridLayout, context: Context, numPlayers: Int, connectionAmount: Int) : this(gridLayout, context) {
        this.numPlayers = numPlayers
        this.connectionAmount = connectionAmount
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
            if (this.checkWin(testRow, column, piece)) {

                this.playerTurn = 0
            }
            else {
                this.playerTurn = getNextPlayer(piece)
            }
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
        return (currentPlayer) % this.numPlayers + 1
    }

    fun checkWin(moveRow: Int, moveColumn: Int, player: Int): Boolean {

        var connections = mutableListOf(0,0,0,0)
        var incrementer = 1
        var pairing = 0

        for (x in -1..1) {
            for (y in -1..1) {
                for (i in 1 until this.connectionAmount) {
                    val testRow = moveRow+x*i
                    val testColumn = moveColumn+y*i

                    // avoid IndexOutOfBounds
                    if (testRow > gridLayout.rowCount-1) break
                    if (testRow < 0) break
                    if (testColumn > gridLayout.columnCount-1) break
                    if (testColumn < 0) break

                    // don't check the piece that was just placed
                    if (x == 0 && y == 0) {
                        // this switches the direction of the accumulating list
                        incrementer=-1
                        break
                    }

                    var boardState = board[testRow][testColumn].state

                    if (boardState == player) {
                        connections[pairing] += 1
                    }
                    else break
                }
                pairing += incrementer
            }
        }
        if (connections.maxOrNull() == connectionAmount-1) {
            return true
        }
        return false
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
            4 -> {
                this.tv.setBackgroundResource(R.drawable.ic_purple_circle)
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

        val bundle: Bundle? = intent.extras
        var playerAmt = bundle?.get("player_amt") as Int
        val connections_to_win = bundle?.get("connection_win") as Int
        val board_width = bundle?.get("board_width") as Int
        val board_height = bundle?.get("board_height") as Int


        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = board_width as Int
        gridLayout.rowCount = board_height as Int

        val layoutParams = gridLayout.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = "${gridLayout.columnCount}:${gridLayout.rowCount}"
        gridLayout.layoutParams = layoutParams

        var players = mapOf(1 to R.color.red, 2 to R.color.yellow)
        when (playerAmt) {
            2 -> players = mapOf(1 to R.color.red, 2 to R.color.yellow)
            3 -> players = mapOf(1 to R.color.red, 2 to R.color.yellow, 3 to R.color.green)
            4 -> players = mapOf(1 to R.color.red, 2 to R.color.yellow, 3 to R.color.green, 4 to R.color.purple_200)
        }
//        val players = mapOf(1 to R.color.red, 2 to R.color.yellow)
//        val players = mapOf(1 to R.color.red, 2 to R.color.yellow, 3 to R.color.green)

        val connectionAmount = connections_to_win

        val board = Board(gridLayout,this, players.size, connectionAmount)

        val slider = Slider(board, seekBar)

        // nextPlayer (End's Turn)
        val turn = findViewById<Button>(R.id.turn)
        turn.setBackgroundColor(ContextCompat.getColor(this, players[1] ?: R.color.black))
        seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_red_circle)
        turn?.setOnClickListener {
            val thisPlayer: Int = board.playerTurn
            val nextPlayer: Int = board.move(slider.columnPosition)
            if (nextPlayer == 0) {
                val intent = Intent(this, EndScreen::class.java)
                intent.putExtra("player_winner", thisPlayer)
                startActivity(intent)
            }
            turn.setBackgroundColor(ContextCompat.getColor(this, players[nextPlayer] ?: R.color.black))
            when (nextPlayer) {
                1 -> seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_red_circle)
                2 -> seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_yellow_circle)
                3 -> seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_green_circle)
                4 -> seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_purple_circle)
                else -> seekBar.thumb = ContextCompat.getDrawable(this, R.drawable.ic_black_circle)
            }
        }

        // clear
        val clearButton = findViewById<Button>(R.id.clear)
        clearButton?.setOnClickListener {
            board.clearBoard()

        }

        // back
        val backButton = findViewById<Button>(R.id.back)
        backButton?.setOnClickListener {
            val intent = Intent(this, GameSettings::class.java)
            startActivity(intent)
        }
    }
}
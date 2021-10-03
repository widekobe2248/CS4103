package com.example.hw4_test

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class Board(val gridLayout: GridLayout, val context: Context) {
    val board : MutableList<MutableList<BoardItem>> = ArrayList()

    init {

        // calculate the desired width and height
        val w : Int = this.gridLayout.layoutParams.width / this.gridLayout.columnCount

        // this uses width because height is not defined in the layout, and it's a square
        val h : Int = this.gridLayout.layoutParams.width / this.gridLayout.rowCount


        // we want the objects to fit on the screen, so take the smaller dimension
        val dimen : Int = if (w < h) w else h

        Log.d("info", "w: ${w}, h: ${h}, dimen: ${dimen}")


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

    fun move(piece : Int, column: Int) {
        var testRow = this.gridLayout.rowCount - 1

        // get the lowest empty tile or 0
        while(this.board[testRow][column].state != 0 && testRow > 0 ) {
            testRow--
        }

        this.board[testRow][column].fillSpot(piece)
    }
}

class BoardItem(val tv: TextView) {
    var state = 0

    // sets the spot's state, if currently empty
    fun fillSpot(piece: Int):Boolean {

        // if spot is taken, do nothing
        if (this.state != 0) return false

        when (piece) {
            1 -> {
                this.tv.setBackgroundResource(R.drawable.ic_red_circle)
                this.state = piece
            }
            2 -> {
                this.tv.setBackgroundResource(R.drawable.ic_yellow_circle)
                this.state = piece
            }
            else -> {
                this.tv.setBackgroundResource(R.drawable.ic_white_circle)
                this.state = 0

            }
        }
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
            else -> {
                this.tv.setBackgroundResource(R.drawable.ic_white_circle)
                this.state = 0

            }
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val textView = findViewById<TextView>(R.id.textView)

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = 9
        gridLayout.rowCount = 6

        val board = Board(gridLayout,this)

        var j = 0

        // red
        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener {

            board.move(1,j)
        }

        // yellow
        val button2 = findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            board.move(2, j)
        }

        // right
        val button3 = findViewById<Button>(R.id.button3)
        button3?.setOnClickListener {

            j = if (j < board.gridLayout.columnCount-1) j+1 else 0

            textView.text = j.toString()
        }

        // left
        val button4 = findViewById<Button>(R.id.button4)
        button4?.setOnClickListener {

            j = if (j > 0 ) j-1 else board.gridLayout.columnCount - 1

            textView.text = j.toString()

        }


    }


}
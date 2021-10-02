package com.example.hw4_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import kotlin.random.Random

class Board(val gridLayout: GridLayout) {
    val board : MutableList<MutableList<BoardItem>> = ArrayList()

//    val row
}

class BoardItem(val tv: TextView) {
//    var tv : TextView = TextView()
    var state = 0
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val textView = findViewById<TextView>(R.id.textView)
//        gridLayout?.setOn

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = 9
        gridLayout.rowCount = 6

//        val sArr = mutableListOf<BoardItem>()
        val board = Board(gridLayout)

        for (i in 0 until gridLayout.rowCount) {
            board.board.add(mutableListOf<BoardItem>())
            for (j in 0 until gridLayout.columnCount) {
                val tv = TextView(this)

                // just for testing, alternate color
//                if (Random.nextInt(0,1)%2==0) {
//                    tv.setBackgroundResource((R.drawable.ic_red_circle))
//                }
//                else {
//                    tv.setBackgroundResource((R.drawable.ic_yellow_circle))
//                }

                tv.setBackgroundResource(R.drawable.ic_white_circle)
                val param = GridLayout.LayoutParams()

                // calculate the desired width and height
                val w : Int = gridLayout.layoutParams.width / gridLayout.columnCount

                // this uses width because height is not defined in the layout, and it's a square
                val h : Int = gridLayout.layoutParams.width / gridLayout.rowCount

                // we want the objects to fit on the screen, so take the smaller dimension
                val dimen : Int = if (w < h) w else h

                // set width and height
                param.width = dimen
                param.height = dimen

                tv.layoutParams = param

                board.board[i].add(BoardItem(tv))
                board.gridLayout.addView(tv)
            }
        }

        var j = 0

        // red
        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener {

            var testRow = board.gridLayout.rowCount - 1

            // get the lowest empty tile or 0
            while(board.board[testRow][j].state != 0) {
                testRow--
                if (testRow < 0) {
                    testRow = 0
                    break
                }
            }

            board.board[testRow][j].tv.setBackgroundResource(R.drawable.ic_red_circle)
            board.board[testRow][j].state = 2
        }

        // yellow
        val button2 = findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {
            var testRow = board.gridLayout.rowCount - 1

            // get the lowest empty tile or 0
            while(board.board[testRow][j].state != 0) {
                testRow--
                if (testRow < 0) {
                    testRow = 0
                    break
                }
            }

            board.board[testRow][j].tv.setBackgroundResource(R.drawable.ic_yellow_circle)
            board.board[testRow][j].state = 2
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
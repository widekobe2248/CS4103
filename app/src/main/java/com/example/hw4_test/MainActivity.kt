package com.example.hw4_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import org.w3c.dom.Text
import kotlin.random.Random

class Board(val gridLayout: GridLayout) {
    val board = mutableListOf<BoardItem>()
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



        var gridLayout = findViewById<GridLayout>(R.id.gridLayout)
//        gridLayout?.setOn

        gridLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        gridLayout.columnCount = 9
        gridLayout.rowCount = 6

//        val sArr = mutableListOf<BoardItem>()
        val board = Board(gridLayout)

        for (i in 0 until gridLayout.rowCount) {
            for (j in 0 until gridLayout.columnCount) {
                val tv: TextView = TextView(this)

                // just for testing, alternate color
                if (Random.nextInt(0,2)%2==0) {
                    tv.setBackgroundResource((R.drawable.ic_red_circle))
                }
                else {
                    tv.setBackgroundResource((R.drawable.ic_yellow_circle))
                }

//                tv.setBackgroundResource(R.drawable.ic_black_circle)
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

                board.board.add(BoardItem(tv))
                board.gridLayout.addView(tv)
            }
        }

        var i = 0

        val button = findViewById<Button>(R.id.button)

        button?.setOnClickListener {

//            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            board.board[i].state = 1
            board.board[i].tv.setBackgroundResource(R.drawable.ic_red_circle)

            i++
            if (i >= board.board.size) {
                i = 0
            }

            board.board[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            board.board[i].state = 0
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {

//            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            board.board[i].state = 2

            board.board[i].tv.setBackgroundResource(R.drawable.ic_yellow_circle)

            i++
            if (i >= board.board.size) {
                i = 0
            }

            board.board[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            board.board[i].state = 0
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3?.setOnClickListener {

//            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
//            board.board[i].state = 2
//
//            board.board[i].tv.setBackgroundResource(R.drawable.ic_yellow_circle)
//
//            i++
//            if (i >= board.board.size) {
//                i = 0
//            }
//
//            board.board[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
//            board.board[i].state = 0
        }


    }


}
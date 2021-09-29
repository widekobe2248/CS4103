package com.example.hw4_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout

class Board(val gridLayout: GridLayout) {
    val board = mutableListOf<BoardItem>()
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

        val sArr = mutableListOf<BoardItem>()

        for (i in 0 until gridLayout.rowCount) {
            for (j in 0 until gridLayout.columnCount) {
                val tv: TextView = TextView(this)
//                tv.gravity = (Gravity.CENTER_HORIZONTAL or Gravity.TOP)
                tv.setBackgroundResource(R.drawable.ic_black_circle)
                val param = GridLayout.LayoutParams()

                param.width = ViewGroup.LayoutParams.WRAP_CONTENT
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT

//                param.height = TableLayout.LayoutParams.WRAP_CONTENT
//                param.width = TableLayout.LayoutParams.WRAP_CONTENT
////                param.columnSpec = GridLayout.spec(j,1f)
//                param.rowSpec = GridLayout.spec(i,1f)


//                tv.layoutParams = GridLayout.LayoutParams(GridLayout.spec(i,1f), GridLayout.spec(j,1f))
                tv.layoutParams = param


                sArr.add(BoardItem(tv))
                gridLayout.addView(tv)
            }
        }

        var i = 0

        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener {

//            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            sArr[i].state = 1
            sArr[i].tv.setBackgroundResource(R.drawable.ic_red_circle)

            i++
            if (i >= sArr.size) {
                i = 0
            }

            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            sArr[i].state = 0
        }

        val button2 = findViewById<Button>(R.id.button2)
        button2?.setOnClickListener {

//            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            sArr[i].state = 2

            sArr[i].tv.setBackgroundResource(R.drawable.ic_yellow_circle)

            i++
            if (i >= sArr.size) {
                i = 0
            }

            sArr[i].tv.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            sArr[i].state = 0
        }

    }


}
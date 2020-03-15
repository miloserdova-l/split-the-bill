package com.example.splitthebillv2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*

const val EXTRA_MESSAGE = "text"

class ScrollingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)


        buttonNext.setOnClickListener() {
            val intent = Intent(this, ScrollingActivity2::class.java).apply {
                putExtra(EXTRA_MESSAGE, textCompany.text.toString())
            }
            startActivity(intent)
        }

        names.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val array_of_names = (names.text)!!.split("\n").toMutableList()
                val n = array_of_names.size
                textCompany.setText("People:")
                for (i in 0 until n) {
                    val cur_text_result = textCompany.text.toString()
                    while (array_of_names[i].contains("  ")) {
                        array_of_names[i] = array_of_names[i].replace("  ", " ")
                    }
                    array_of_names[i].trim()
                    if (array_of_names[i].length != 0 && array_of_names[i][0] == ' ')
                        array_of_names[i] = array_of_names[i].replace(" ", "")
                    if (array_of_names[i].isNotEmpty()) {
                        textCompany.setText("${cur_text_result}\n${array_of_names[i]}")
                    }
                }

            }
        })
    }
}



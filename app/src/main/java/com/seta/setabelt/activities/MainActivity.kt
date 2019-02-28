package com.seta.setabelt.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.seta.setabelt.R
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onWbAvClick(view: View) {
        startActivity<WbAvActivity>()
    }
}

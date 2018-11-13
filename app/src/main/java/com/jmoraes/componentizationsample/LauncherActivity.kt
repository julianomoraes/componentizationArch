package com.jmoraes.componentizationsample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        findViewById<Button>(R.id.button_basic).setOnClickListener {
            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
        }

        findViewById<Button>(R.id.button_player).setOnClickListener {
            startActivity(Intent(this@LauncherActivity, PlayerActivity::class.java))
        }
    }
}

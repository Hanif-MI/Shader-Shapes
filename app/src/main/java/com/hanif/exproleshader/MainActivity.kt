package com.hanif.exproleshader

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mySecondButton = findViewById<View>(R.id.shader1)
        mySecondButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.shader1 -> {
                this.startActivity(Intent(this, Shader1::class.java))
            }
        }
    }
}
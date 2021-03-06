package br.com.hussan.mqttandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.hussan.mqttandroid.coffee.CoffeeActivity
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_main.btnChat
import kotlinx.android.synthetic.main.activity_main.btnCoffee
import kotlinx.android.synthetic.main.activity_main.btnSensor
import kotlinx.android.synthetic.main.activity_main.btnVoice

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        btnSensor.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }

        btnVoice.setOnClickListener {
            val intent = Intent(this, VoiceActivity::class.java)
            startActivity(intent)
        }

        btnCoffee.setOnClickListener {
            val intent = Intent(this, CoffeeActivity::class.java)
            startActivity(intent)
        }
    }
}


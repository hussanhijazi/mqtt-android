package br.com.hussan.mqttandroid

import android.os.Bundle
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_sensor.txtTemperature
import org.eclipse.paho.client.mqttv3.MqttMessage

class SensorActivity : BaseActivity() {

    companion object {
        const val TAG = "SensorActivity"
    }

    init {
        topic = "hussanhijazitemperature"
    }

    override fun messageCallBack(msg: MqttMessage) {
        txtTemperature.text = String(msg.payload)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
    }

}


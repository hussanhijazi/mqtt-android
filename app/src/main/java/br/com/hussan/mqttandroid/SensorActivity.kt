package br.com.hussan.mqttandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.hussan.mqttandroid.mqtt.MqttClient
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_sensor.txtHumidity
import kotlinx.android.synthetic.main.activity_sensor.txtTemperature
import org.eclipse.paho.client.mqttv3.MqttMessage

class SensorActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SensorActivity"
        const val TEMPERATURE_TOPIC = "t0th/temperature"
        const val HUMIDITY_TOPIC = "t0th/humidity"
        const val BROKER: String = "tcp://broker.hivemq.com"

    }

    val mqttClient by lazy {
        MqttClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        mqttClient.connect(BROKER)
        mqttClient.setCallBack(arrayOf(TEMPERATURE_TOPIC, HUMIDITY_TOPIC), ::setData)

    }

    private fun setData(topic: String, msg: MqttMessage) {
        when (topic) {
            TEMPERATURE_TOPIC -> {
                txtTemperature.text = " ${String(msg.payload)} ° c"
            }
            else -> {
                txtHumidity.text = " ${String(msg.payload)}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }
}


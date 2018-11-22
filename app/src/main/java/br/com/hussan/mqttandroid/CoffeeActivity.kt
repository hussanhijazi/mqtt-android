package br.com.hussan.mqttandroid

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_coffee.btnCoffee
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class CoffeeActivity : AppCompatActivity() {

    val mqttClient by lazy {
        MqttClient(this)
    }

    companion object {
        const val TAG = "CoffeeActivity"
        const val COFFEE_COPIC = "gdgfoz/coffeeiot"
    }

    var onOff = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coffee)

        mqttClient.connect()

        mqttClient.client.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                mqttClient.subscribeTopic(COFFEE_COPIC)
            }

            override fun connectionLost(cause: Throwable) {
                Log.d(MqttClient.TAG, "The Connection was lost.")
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.d(MqttClient.TAG, "Incoming message from $topic: " + message.toString())
                updateButton(topic, message)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {

            }
        })

        btnCoffee.setOnClickListener {
            onOff = if (!onOff) {
                mqttClient.publishMessage(COFFEE_COPIC, "on")
//                btnCoffee.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                true
            } else {
                mqttClient.publishMessage(COFFEE_COPIC, "off")
//                btnCoffee.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
                false
            }
        }
    }

    private fun updateButton(topic: String, message: MqttMessage) {
        if (String(message.payload) == "on") {
            btnCoffee.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
            onOff = true
        } else {
            btnCoffee.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
            onOff = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }

}

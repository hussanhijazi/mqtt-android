package br.com.hussan.mqttandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.hussan.mqttandroid.mqtt.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

abstract class BaseActivity : AppCompatActivity() {

    lateinit var topic: Array<String>

    val mqttClient by lazy {
        MqttClient(this)
    }

    companion object {
        const val TAG = "BaseActivity"
    }

    abstract fun messageCallBack(topic: String, msg: MqttMessage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mqttClient.setCallBack(topic, ::messageCallBack)

    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }
}


package br.com.hussan.mqttandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

abstract class BaseActivity : AppCompatActivity() {

    private val client by lazy {
        val clientId = MqttClient.generateClientId()
        MqttAndroidClient(this.applicationContext, "tcp://test.mosquitto.org:1883",
                clientId)
    }
    lateinit var topic: String

    companion object {
        const val TAG = "BaseActivity"
    }

    abstract fun messageCallBack(msg: MqttMessage)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val token = client.connect()
        
            client.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                    subscribeTopic(topic)
                    addLog("Connected to: $serverURI")

                }

                override fun connectionLost(cause: Throwable) {
                    addLog("The Connection was lost.")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    addLog("Incoming message: " + message.toString())
                    messageCallBack(message)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {

                }
            })


        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }

    fun publishMessage(topic: String, msg: String) {

        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            client.publish(topic, message)
            addLog("Message Published")
            if (!client.isConnected) {
                addLog("${client.getBufferedMessageCount()} messages in buffer.")
            }
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }

    }

    fun addLog(log: String) {
        Log.d(TAG, log)
    }

    fun subscribeTopic(topic: String) {
        val token2 = client.subscribe(topic, 0)

        token2.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                addLog("Subscribed!")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                addLog("Failed to subscribe")
                exception.printStackTrace()
            }

        }
    }
}


package br.com.hussan.mqttandroid

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttClient(private val context: Context) {
    private val client by lazy {
        val clientId = MqttClient.generateClientId()
        MqttAndroidClient(context, "tcp://test.mosquitto.org:1883",
                clientId)
    }

    companion object {
        const val TAG = "MqttClient"
    }

    fun connect(topic: String, messageCallBack: (message: MqttMessage) -> Unit) {
        try {
            client.connect()
            client.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                    subscribeTopic(topic)
                    Log.d(TAG, "Connected to: $serverURI")
                }

                override fun connectionLost(cause: Throwable) {
                    Log.d(TAG, "The Connection was lost.")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    Log.d(TAG, "Incoming message from $topic: " + message.toString())
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
            Log.d(TAG, "Message Published to $topic")
            if (!client.isConnected) {
                Log.d(TAG, "${client.getBufferedMessageCount()} messages in buffer.")
            }
        } catch (e: MqttException) {
            Log.d(TAG, "Error Publishing to $topic: " + e.message)
            e.printStackTrace()
        }

    }

    private fun subscribeTopic(topic: String) {
        client.subscribe(topic, 0).actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.d(TAG, "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.d(TAG, "Failed to subscribe to $topic")
                exception.printStackTrace()
            }
        }
    }

    fun close() {
        client.apply {
            unregisterResources()
            close()
        }
    }

}

package br.com.hussan.mqttandroid

import android.os.Bundle
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_chat.btnSend
import kotlinx.android.synthetic.main.activity_chat.etText
import kotlinx.android.synthetic.main.activity_chat.txtMsg
import org.eclipse.paho.client.mqttv3.MqttMessage

class ChatActivity : BaseActivity() {

    companion object {
        const val TAG = "ChatActivity"
    }

    init {
        topic = "andrehussanchat"
    }

    override fun messageCallBack(msg: MqttMessage) {
        txtMsg.text = String(msg.payload)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        btnSend.setOnClickListener {
            if (etText.text.toString().isNotEmpty()) {
                mqttClient.publishMessage("hussanandrechat", etText.text.toString())
                etText.setText("")
            }
        }
    }
}


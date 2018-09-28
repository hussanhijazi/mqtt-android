package br.com.hussan.mqttandroid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_voice.btnVoice
import kotlinx.android.synthetic.main.activity_voice.txtMsg
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.Locale

class VoiceActivity : BaseActivity() {

    companion object {
        const val TAG = "VoiceActivity"
        const val REQ_SPEECH_RESULT = 123
    }

    init {
        topic = "topic"
    }

    override fun messageCallBack(msg: MqttMessage) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)

        btnVoice.setOnClickListener {
            startVoiceCommand()
        }
    }

    private fun startVoiceCommand() {
        Log.d(TAG, "Starting Voice intent...")
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("pt", "BR"))
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_prompt))
        try {
            startActivityForResult(i, REQ_SPEECH_RESULT)
        } catch (e: Exception) {

        }

    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SPEECH_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Request speech result..")
                data?.let {
                    val results = it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val command = results[0]
                    Log.d(TAG, "Current command [$command]")
                    txtMsg.text = command
                    when {
                        command.contains(getString(R.string.on)) -> Log.d(TAG, "Sending on...")
                        command.contains(getString(R.string.off)) -> Log.d(TAG, "Sending ogg...")
                        else -> {
                        }
                    }
                }
            }
        }
    }
}


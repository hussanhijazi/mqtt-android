package br.com.hussan.mqttandroid.coffee

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import br.com.hussan.mqttandroid.extensions.color
import br.com.hussan.mqttandroid.extensions.hide
import br.com.hussan.mqttandroid.extensions.show
import br.com.hussan.mqttandroid.mqtt.MqttClient
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_coffee.btnCoffee
import kotlinx.android.synthetic.main.activity_coffee.imgCoffee
import kotlinx.android.synthetic.main.activity_coffee.lytRoot
import kotlinx.android.synthetic.main.activity_coffee.progressBar
import org.eclipse.paho.client.mqttv3.MqttMessage

class CoffeeActivity : AppCompatActivity() {

    private var broker: String = "tcp://broker.hsivemq.com"
    private var topic: String = "gdgfoz/coffeesiot"

    val mqttClient: MqttClient by lazy {
        MqttClient(this)
    }

    companion object {
        const val COFFEE_COPIC = "gdgfoz/coffeesiot"
        const val SHARED_PREFS = "SHARED_PREFS"

    }

    var onOff = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coffee)

        getLocalData()

        connectAndSubscribe()

        btnCoffee.setOnClickListener {
            progressBar.show()
            onOff = if (!onOff) {
                mqttClient.publishMessage(topic, "on")
                true
            } else {
                mqttClient.publishMessage(topic, "off")
                false
            }
        }
    }

    private fun getLocalData() {
        val sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE) ?: return
        broker = sharedPref.getString(getString(R.string.broker), "tcp://broker.hivemq.com:1883")
        topic = sharedPref.getString(getString(R.string.topic), COFFEE_COPIC)
    }

    private fun connectAndSubscribe() {
        mqttClient.connect(broker)
        mqttClient.setCallBack(arrayOf(topic), ::updateButton)
    }

    private fun updateButton(topic: String, message: MqttMessage) {
        imgCoffee.show()
        onOff = if (String(message.payload) == "on") {
            btnCoffee.setColorFilter(color(R.color.colorPrimary))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@CoffeeActivity, R.drawable.smile))
            lytRoot.setBackgroundColor(color(android.R.color.white))

            true
        } else {
            btnCoffee.setColorFilter(color(android.R.color.darker_gray))
            imgCoffee.setImageDrawable(ContextCompat.getDrawable(this@CoffeeActivity, R.drawable.upside_down_smile))
            lytRoot.setBackgroundColor(color(android.R.color.black))
            false
        }
        progressBar.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.close()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.config -> {
                val intent = Intent(this, ConfigActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getLocalData()
                mqttClient.close()
                mqttClient.disconnect()
                connectAndSubscribe()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_config, menu)
        return true
    }
}

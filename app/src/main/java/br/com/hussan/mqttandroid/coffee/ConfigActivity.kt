package br.com.hussan.mqttandroid.coffee

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.hussan.mqttandroid.coffee.CoffeeActivity.Companion.SHARED_PREFS
import com.example.hussan.mqttandroid.R
import kotlinx.android.synthetic.main.activity_config.btnSave
import kotlinx.android.synthetic.main.activity_config.edtBroker
import kotlinx.android.synthetic.main.activity_config.edtTopic


class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_config)
        btnSave.setOnClickListener {
            if (edtBroker.text?.isEmpty() == true
                    || edtTopic.text?.isEmpty() == true) {
                Toast.makeText(this, R.string.type_all_fields, Toast.LENGTH_LONG).show()
            } else {
                val sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

                with(sharedPref.edit()) {
                    putString(getString(R.string.broker), "tcp://${edtBroker.text.toString().trim()}")
                    putString(getString(R.string.topic), edtTopic.text.toString())
                    commit()
                }
                val intent = intent
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}

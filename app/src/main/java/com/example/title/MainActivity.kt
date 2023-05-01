package com.example.title

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fuel.httpGet
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    private var handleCommandReceiver: BroadcastReceiver? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getButton = findViewById<Button>(R.id.getTitleButton)
        getButton.setOnClickListener {
            onGetTitleButtonClicked()
        }

        val shareButton = findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener {
            onShareClicked()
        }

//        val filter = IntentFilter()
//        filter.addAction("service.to.activity.transfer")
//        handleCommandReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val local = Intent()
//                local.action = "activity.to.service.transfer"
//                local.putExtra("title", title)
//                context.sendBroadcast(local)
//            }
//        }
//        registerReceiver(handleCommandReceiver, filter)
    }

    private fun onGetTitleButtonClicked() {
        val urlEditText = findViewById<View>(R.id.urlEditText) as EditText
        val urlString = urlEditText.text.toString()

        runBlocking {
            val body = urlString.httpGet().body
            title = Jsoup.parse(body).title()
            runOnUiThread {
                val titleView = findViewById<TextView>(R.id.titleTextView)
                titleView.text = title
            }
        }
    }

    private fun onShareClicked() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, title)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, "Поделиться с"))
    }
}
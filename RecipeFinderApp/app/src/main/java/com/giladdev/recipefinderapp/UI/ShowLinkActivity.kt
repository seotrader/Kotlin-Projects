package com.giladdev.recipefinderapp.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.giladdev.recipefinderapp.R
import kotlinx.android.synthetic.main.activity_show_link.*

class ShowLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_link)

        var extra = intent.extras

        if (extra != null){
            var link = extra.get("link")

            webView.loadUrl(link.toString())
        }
    }
}

package com.example.eventsapi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eventsapi.R
import com.example.eventsapi.model.Event
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class EventDetailsActivity : AppCompatActivity() {

    lateinit var ivEvent : ImageView
    lateinit var tvEventTitle: TextView
    lateinit var tvEventPrice : TextView
    lateinit var tvEventDescription : TextView
    lateinit var tvEventDate : TextView
    lateinit var btCheckin : Button
    lateinit var event : Event
    lateinit var timeoutLayout : ConstraintLayout
    lateinit var progressBar : ProgressBar

    val CHECKIN_URL = "http://5b840ba5db24a100142dcd8c.mockapi.io/api/checkin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        event = intent.extras?.getSerializable("event") as Event

        setContentView(R.layout.activity_event_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ivEvent = findViewById(R.id.ivEvent)
        tvEventTitle = findViewById(R.id.tvEventTitle)
        tvEventPrice = findViewById(R.id.tvEventPrice)
        tvEventDescription = findViewById(R.id.tvEventDescription)
        tvEventDate = findViewById(R.id.tvEventDate)
        btCheckin = findViewById(R.id.btCheckin)

        timeoutLayout = findViewById(R.id.timeoutLayout)
        progressBar = findViewById(R.id.progressBar)

        Picasso.get().load(event.imageURL).resize(150,150).into(ivEvent)
        tvEventTitle.text = event.title.capitalize()
        tvEventPrice.text = event.price
        tvEventDescription.text = event.description
        tvEventDate.text = event.date

        btCheckin.setOnClickListener {
            checkinEvent()
        }

        progressBar.visibility = View.VISIBLE

    }


    fun checkinEvent(){
        val httpClient = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build()

        val builder = FormBody.Builder()
        val formBody = builder.build()

        val request = Request.Builder()
            .url(CHECKIN_URL)
            .post(formBody)
            .build()


        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("EventDetailsActivity",e.message + "TIMEOUT TIMEOUT")
                timeoutLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                System.out.println("FOI")
            }
        })
    }


    fun shareContent(){
        var content = event.title

        content += "\nDescription: ${event.description}"
        content += "\nPrice: ${event.price} R$"
        content += "\nDate: ${event.date}"

        Log.i("Details",content)

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT,content)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share"))
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        when(id){
            R.id.action_share -> shareContent()
        }

        return super.onOptionsItemSelected(item)
    }

}
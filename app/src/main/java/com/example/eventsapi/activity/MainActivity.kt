package com.example.eventsapi.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eventsapi.R
import com.example.eventsapi.activity.adapter.EventListAdapter
import com.example.eventsapi.model.Event
import kotlinx.android.synthetic.main.activity_main.*


import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    lateinit var gridViewEvents : GridView
    var eventsArray = arrayListOf<Event>()
    lateinit var eventAdapter : EventListAdapter
    lateinit var timeoutLayout : ConstraintLayout
    lateinit var progressBar : ProgressBar

    val EVENT_URL : String = "http://5b840ba5db24a100142dcd8c.mockapi.io/api/events"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as Toolbar?)

        progressBar = findViewById(R.id.progressBar)
        timeoutLayout = findViewById(R.id.timeoutLayout)
        gridViewEvents = findViewById(R.id.gridViewTypes)
        gridViewEvents.setOnItemClickListener(this)
        eventAdapter = EventListAdapter(this,eventsArray)
        gridViewEvents.adapter = eventAdapter

        progressBar.visibility = View.VISIBLE

        //OkHttp
        val httpClient = OkHttpClient.Builder().connectTimeout(5,TimeUnit.SECONDS).build()
        val request = Request.Builder().url(EVENT_URL).build()

        httpClient.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                Log.i("MainActivity",e.message + "TIMEOUT TIMEOUT")
                timeoutLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string() ?: "{}"

                val jsonResponse = JSONObject(responseString)
                val eventsJsonArray = jsonResponse.getJSONArray("")

                for(i in 0 until eventsJsonArray.length()){
                    val typeJsonObj = eventsJsonArray.getJSONObject(i)
                    val eventID = typeJsonObj.getString("eventId")
                    val title = typeJsonObj.getString("title")
                    val price = typeJsonObj.getString("price")
                    val description = typeJsonObj.getString("description")
                    val imageURL = typeJsonObj.getString("image")
                    val date = typeJsonObj.getString("date")
                    val typeObj = Event(eventID, title, price, description, imageURL, date)
                    eventsArray.add(typeObj)
                }

                //Atualiza a interface
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    eventAdapter.notifyDataSetChanged()
                }
            }

        })
    }



    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val editor = getSharedPreferences("event", Context.MODE_PRIVATE).edit()
        editor.putString("eventID",eventsArray.get(position).eventId)
        editor.putString("title",eventsArray.get(position).title)
        editor.putString("price",eventsArray.get(position).price)
        editor.putString("description",eventsArray.get(position).description)
        editor.putString("imageURL",eventsArray.get(position).imageURL)
        editor.putString("date",eventsArray.get(position).date)
        editor.commit()

        val intent = Intent(this, EventDetailsActivity::class.java)
        startActivity(intent)
    }

}




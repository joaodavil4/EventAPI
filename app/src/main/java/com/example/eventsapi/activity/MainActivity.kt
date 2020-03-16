package com.example.eventsapi.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eventsapi.R
import com.example.eventsapi.activity.adapter.EventListAdapter
import com.example.eventsapi.model.Event
import okhttp3.*
import org.json.JSONArray
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
                val eventsJsonArray = JSONArray(responseString)

                for(i in 0 until eventsJsonArray.length()){
                    val eventJsonObject = eventsJsonArray.getJSONObject(i)
                        val peopleInfo = eventJsonObject.getJSONArray("people")
                        val eventID = peopleInfo.getJSONObject(0).getString("eventId")
                    val title = eventJsonObject.getString("title")
                    val price = eventJsonObject.getString("price")
                    val description = eventJsonObject.getString("description")
                    val imageURL = eventJsonObject.getString("image")
                    val date = eventJsonObject.getString("date")
                    val eventObj = Event(eventID, title, price, description, imageURL, date)
                    eventsArray.add(eventObj)
                }

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    eventAdapter.notifyDataSetChanged()
                }
            }

        })
    }



    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val bundle = Bundle()
//        bundle.putString("eventID",eventsArray.get(position).eventId)
//        bundle.putString("title",eventsArray.get(position).title)
//        bundle.putString("price",eventsArray.get(position).price)
//        bundle.putString("description",eventsArray.get(position).description)
//        bundle.putString("imageURL",eventsArray.get(position).imageURL)
//        bundle.putString("date",eventsArray.get(position).date)
        bundle.putSerializable("event", eventsArray.get(position))

        val intent = Intent(this, EventDetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}




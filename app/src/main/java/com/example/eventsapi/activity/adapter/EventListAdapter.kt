package com.example.eventsapi.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.eventsapi.R
import com.example.eventsapi.model.Event
import com.squareup.picasso.Picasso

class EventListAdapter(context: Context, val objects: ArrayList<Event>) : ArrayAdapter<Event>(context, 0, objects) {

    var inflater : LayoutInflater

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val holder : ViewHolder

        val eventObj = this.objects[position]

        if (convertView == null){
            view = inflater.inflate(R.layout.event_adapter,parent,false)
            holder = ViewHolder(view = view)
            view.tag = holder
        }else{
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.tvEventTitle.text = eventObj.title
        Picasso.get().load(eventObj.imageURL).resize(150,150).into(holder.eventImage)

        return view
    }


    class ViewHolder(view: View){
        var tvEventTitle : TextView = view.findViewById(R.id.tvEventTitle)
        var eventImage : ImageView = view.findViewById(R.id.eventBackground)
    }

}
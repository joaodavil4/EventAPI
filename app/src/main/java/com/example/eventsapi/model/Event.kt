package com.example.eventsapi.model

import java.io.Serializable

class Event( var eventId : String = "", var title : String = "", var price : String = "",
             var description : String = "", var imageURL : String = "", var date : String = "") : Serializable
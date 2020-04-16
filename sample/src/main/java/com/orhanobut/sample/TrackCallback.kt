package com.orhanobut.sample

interface TrackCallback {
  fun onEventImme(eventId: String, eventParam: Map<String, String>?)
}
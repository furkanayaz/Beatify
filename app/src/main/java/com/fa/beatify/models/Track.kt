package com.fa.beatify.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Track (
  @SerializedName("data")
  @Expose
  var data: ArrayList<TrackModel>? = null,
  @SerializedName("total")
  @Expose
  var total: Int? = null
)
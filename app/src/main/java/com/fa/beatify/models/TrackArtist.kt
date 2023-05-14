package com.fa.beatify.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TrackArtist (
  @SerializedName("id")
  @Expose
  var id: Int? = null,
  @SerializedName("name")
  @Expose
  var name: String? = null,
  @SerializedName("tracklist")
  @Expose
  var tracklist: String? = null,
  @SerializedName("type")
  @Expose
  var type: String? = null
)
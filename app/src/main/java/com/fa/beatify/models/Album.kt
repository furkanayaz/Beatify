package com.fa.beatify.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Album (
  @SerializedName("data")
  @Expose
  var data: ArrayList<AlbumModel>? = null,
  @SerializedName("total")
  @Expose
  var total: Int? = null,
  @SerializedName("next")
  @Expose
  var next: String? = null
)
package com.fa.beatify.utils

class ImageRepo {
    fun getImage(md5Image: String): String = "https://e-cdns-images.dzcdn.net/images/cover/$md5Image/500x500-000000-80-0-0.jpg"
}
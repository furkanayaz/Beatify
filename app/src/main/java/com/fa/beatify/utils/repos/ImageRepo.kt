package com.fa.beatify.utils.repos

class ImageRepo {

    val getImage: (String) -> String = { md5Image: String -> "https://e-cdns-images.dzcdn.net/images/cover/$md5Image/500x500-000000-80-0-0.jpg" }

}
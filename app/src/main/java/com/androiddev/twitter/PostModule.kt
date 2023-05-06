package com.androiddev.twitter

class PostModule {
    var uname: String? = null
    var image: String? = null
    var text: String? = null
    var time: String? = null
    var id: String? = null
    var likes = 0

    constructor(
        uname: String?,
        image: String?,
        text: String?,
        likes: Int,
        time: String?,
        id: String?
    ) {
        this.uname = uname
        this.image = image
        this.text = text
        this.likes = likes
        this.time = time
        this.id = id
    }

    constructor(text: String?) {
        this.text = text
    }

    constructor() {}
}
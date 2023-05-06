package com.androiddev.twitter

class UserModule {
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var password: String? = null

    constructor(name: String?, phone: String?, email: String?, password: String?, image: String?) {
        this.name = name
        this.phone = phone
        this.email = email
        this.password = password
        this.image = image
    }

    var image: String? = null

    constructor(name: String?, email: String?) {
        this.name = name
        this.email = email
    }

    constructor() {}
}
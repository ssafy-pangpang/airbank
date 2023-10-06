package com.example.myapplication.model

enum class State {
    SUCCESS,
    ERROR,
    LOADING
}

class Resource<T>(val status: State?, val data: T?, val message: String?) {

    fun <T> success(data: T?): Resource<T> {
        return Resource(State.SUCCESS, data, null)
    }

    fun <T> error(message: String): Resource<T> {
        return Resource(State.ERROR, null, "Error !")
    }

    fun <T> loading(): Resource<T> {
        return Resource(State.LOADING, null, null)
    }
}



package com.example.tickets.model.network

import com.example.tickets.model.entity.Offer
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ApiClient {

    private val gson = Gson()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://my-json-server.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    val client = retrofit.create(TicketApi::class.java)

    /**
     * think about performing network request
     */
}

interface TicketApi {

    @GET("estharossa/fake-api-demo/offer_list")
    fun getList(): Call<List<Offer>>
}
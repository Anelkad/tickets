package com.example.tickets.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickets.model.entity.Offer
import com.example.tickets.model.network.ApiClient.client
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OfferListViewModel : ViewModel() {

    private val _offerList = MutableLiveData<List<Offer>>()
    val offerList: LiveData<List<Offer>> = _offerList

    private val _errorMessage = Channel<String>()
    val errorMessage: Flow<String> = _errorMessage.receiveAsFlow()


    private val handler = CoroutineExceptionHandler { context, exception ->
        viewModelScope.launch {
            _errorMessage.send(exception.message ?: "Unknown error")
        }
        println("Caught $exception in ${context[CoroutineName]}")
    }

    init {
        fetchList()
    }

    private fun fetchList() {
        viewModelScope.launch(handler) {
            val deferred = async {
                delay(15000L)
                val response = client.getList()
                if (response.isSuccessful) {
                    _offerList.postValue(response.body())
                } else {
                    println("Error: ${response.code()}")
                    _errorMessage.send(response.message())
                }
            }
            val cancelDeffered = async {
                delay(5000L)
                deferred.cancel()
            }
            listOf(deferred, cancelDeffered).awaitAll()
        }
    }
}
package com.example.tickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.tickets.R
import com.example.tickets.adapter.OfferListAdapter
import com.example.tickets.databinding.FragmentOfferListBinding
import com.example.tickets.model.entity.Offer
import com.example.tickets.model.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private var list: MutableList<Offer> = mutableListOf()

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        ApiClient.client.getList().enqueue(object : Callback<List<Offer>> {
            override fun onResponse(p0: Call<List<Offer>>, p1: Response<List<Offer>>) {
                println("RetrofitRequest: ${p1.body()}")

                p1.body()?.let { offers ->
                    list = offers.toMutableList()
                }

                adapter.submitList(list)
            }

            override fun onFailure(p0: Call<List<Offer>>, p1: Throwable) {
                println("RetrofitRequest: ${p1}")
            }

        })

//        adapter.setItems(FakeService.offerList)
    }

    private fun setupUI() {
        with(binding) {

            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sort_by_price -> {
                       adapter.submitList(list.sortedBy { it.price })
                    }

                    R.id.sort_by_duration -> {
                        adapter.submitList(list.sortedBy { it.flight.duration })
                        /**
                         * implement sorting by duration
                         * hint: you can take the current list using getCurrentList method of ListAdapter instance
                         */
                    }
                }
            }
        }
    }
}
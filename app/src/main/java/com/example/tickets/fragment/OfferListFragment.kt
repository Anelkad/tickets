package com.example.tickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tickets.R
import com.example.tickets.adapter.OfferListAdapter
import com.example.tickets.databinding.FragmentOfferListBinding
import com.example.tickets.model.entity.Offer
import com.example.tickets.model.network.ApiClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private val viewModel: OfferListViewModel by viewModels()

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
        viewModel.offerList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            list = it.toMutableList()
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collectLatest { message ->
               Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
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
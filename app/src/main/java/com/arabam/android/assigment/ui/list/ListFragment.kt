package com.arabam.android.assigment.ui.list

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arabam.android.assigment.R
import com.arabam.android.assigment.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel by activityViewModels<ListViewModel>()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)
        val adapter = CarListAdapter()
        val sortBottomSheetDialog = SortBottomSheetFragment()
        val filterBottomSheetDialog = FilterAdvertsBottomSheetFragment()

        (activity as AppCompatActivity).setSupportActionBar(requireActivity().findViewById(R.id.list_fragment_toolbar))
        val actionbar = (activity as AppCompatActivity).supportActionBar

        if (actionbar != null)
            actionbar.title = resources.getString(R.string.list_fragment_toolbar)

        binding.apply {

            listRecyclerview.setHasFixedSize(true)
            listRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            listRecyclerview.itemAnimator = null
            listRecyclerview.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CarLoadStateAdapter { adapter.retry() },
                footer = CarLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }

            listSortLayout.setOnClickListener {
                if (!sortBottomSheetDialog.isAdded)
                    activity?.supportFragmentManager?.let { it1 ->
                        sortBottomSheetDialog.show(it1, TAG)
                    }
            }
            listFilterLayout.setOnClickListener {
                if (!filterBottomSheetDialog.isAdded)
                    activity?.supportFragmentManager?.let { it1 ->
                        filterBottomSheetDialog.show(it1, TAG)
                    }
            }

            adapter.onItemSelectListener = {
                val action = it?.id?.let { it1 ->
                        ListFragmentDirections.actionListFragmentToDetailFragment(it1)
                    }
                if (hasNetworkAvailable()) {
                    if (action != null) findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.network_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            filterBottomSheetDialog.onItemSelectListener = {
                val filter = it!!
                viewModel.setFilterDateQueries(filter.minDate, filter.maxDate)
                viewModel.setYearQueries(filter.minYear, filter.maxYear)
                adapter.refresh()
            }
            sortBottomSheetDialog.onItemSelectListener = {
                val sort = it!!
                viewModel.setSortQueries(sort.sort, sort.sortDirection)
                adapter.refresh()
            }
        }

        viewModel.cars.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                listRecyclerview.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                    listRecyclerview.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    private fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context?.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}
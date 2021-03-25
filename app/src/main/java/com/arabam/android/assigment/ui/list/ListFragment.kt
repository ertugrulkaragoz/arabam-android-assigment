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
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arabam.android.assigment.R
import com.arabam.android.assigment.databinding.FragmentListBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.arabam.android.assigment.data.local.CarDetailDao
import com.arabam.android.assigment.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel by viewModels<ListViewModel>()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var carDetailDao: CarDetailDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)
        val carListAdapter = CarListAdapter()
        val sortBottomSheetDialog = SortBottomSheetFragment()
        val filterBottomSheetDialog = FilterAdvertsBottomSheetFragment()

        (activity as AppCompatActivity).setSupportActionBar(requireActivity().findViewById(R.id.list_fragment_toolbar))
        val actionbar = (activity as AppCompatActivity).supportActionBar

        if (actionbar != null)
            actionbar.title = resources.getString(R.string.list_fragment_toolbar)

        binding.apply {
            listRecyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = null
                adapter = carListAdapter.withLoadStateHeaderAndFooter(
                    header = CarLoadStateAdapter { carListAdapter.retry() },
                    footer = CarLoadStateAdapter { carListAdapter.retry() }
                )
            }

            buttonRetry.setOnClickListener { carListAdapter.retry() }

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
        }

        viewModel.cars.observe(viewLifecycleOwner) {
            carListAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        carListAdapter.addLoadStateListener { loadState ->
            binding.apply {
                when (val refresh = loadState.mediator?.refresh) {
                    is LoadState.Loading -> {
                        textViewError.isVisible = false
                        buttonRetry.isVisible = false
                        progressBar.isVisible = true
                        textViewNoResults.isVisible = false

                        if (!viewModel.newQueryInProgress && carListAdapter.itemCount > 0) {
                            listRecyclerview.show()
                        } else {
                            listRecyclerview.invisible()
                        }
                    }
                    is LoadState.NotLoading -> {
                        textViewError.isVisible = false
                        buttonRetry.isVisible = false
                        progressBar.isVisible = false
                        listRecyclerview.isVisible = carListAdapter.itemCount > 0

                        val noResults =
                            carListAdapter.itemCount < 1 && loadState.append.endOfPaginationReached
                                    && loadState.source.append.endOfPaginationReached

                        textViewNoResults.isVisible = noResults

                        viewModel.newQueryInProgress = false
                    }
                    is LoadState.Error -> {
                        textViewNoResults.isVisible = false
                        listRecyclerview.isVisible = carListAdapter.itemCount > 0

                        val noCachedResults =
                            carListAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                        textViewError.isVisible = noCachedResults
                        buttonRetry.isVisible = noCachedResults

                        val errorMessage = getString(
                            R.string.could_not_load_search_results,
                            refresh.error.localizedMessage
                                ?: getString(R.string.unknown_error_occurred)
                        )
                        textViewError.text = errorMessage

                        viewModel.newQueryInProgress = false
                    }
                }
            }
        }

        carListAdapter.onItemSelectListener = {
            val id = it ?: 0
            val action = it?.let { carId ->
                ListFragmentDirections.actionListFragmentToDetailFragment(carId)
            }

            CoroutineScope(Dispatchers.IO).launch {
                if (hasNetworkAvailable() || carDetailDao.checkIdIfExist(id)) {
                    if (action != null)  findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(),resources.getString(R.string.network_not_available),Toast.LENGTH_SHORT).show()
                }
            }
        }

        filterBottomSheetDialog.onItemSelectListener = {
            val filter = it!!
            viewModel.setFilterDateQueries(filter.minDate, filter.maxDate)
            viewModel.setYearQueries(filter.minYear, filter.maxYear)
            carListAdapter.refresh()
        }

        sortBottomSheetDialog.onItemSelectListener = {
            val sort = it!!
            viewModel.setSortQueries(sort.sort, sort.sortDirection)
            carListAdapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = context?.getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }

        companion object {
        private const val TAG = "ListFragment"
    }
}
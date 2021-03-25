package com.arabam.android.assigment.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.model.Filter
import com.arabam.android.assigment.databinding.FragmentFilterAdvertsBottomSheetBinding
import com.arabam.android.assigment.ui.list.ListViewModel.Companion.DEFAULT_MAX_DATE
import com.arabam.android.assigment.ui.list.ListViewModel.Companion.DEFAULT_MAX_YEAR
import com.arabam.android.assigment.ui.list.ListViewModel.Companion.DEFAULT_MIN_DATE
import com.arabam.android.assigment.ui.list.ListViewModel.Companion.DEFAULT_MIN_YEAR
import com.arabam.android.assigment.util.MaterialDialog
import com.arabam.android.assigment.util.MaterialDialog.Companion.positiveButton
import com.arabam.android.assigment.util.MaterialDialog.Companion.singleChoiceItems
import com.arabam.android.assigment.util.MaterialDialog.Companion.title
import com.arabam.android.assigment.util.MaterialDialog.Companion.view
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FilterAdvertsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<ListViewModel>()
    private var _binding: FragmentFilterAdvertsBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var checkedItem = 0
    private var minDateQuery = DEFAULT_MIN_DATE
    private var maxDateQuery = DEFAULT_MAX_DATE
    private var minYearQuery = DEFAULT_MIN_YEAR
    private var maxYearQuery = DEFAULT_MAX_YEAR

    var onItemSelectListener: ((Filter?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_adverts_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFilterAdvertsBottomSheetBinding.bind(view)

        val filterDateItems = arrayOf(
            resources.getString(R.string.filter_adverts_all),
            resources.getString(R.string.filter_adverts_last_one_day),
            resources.getString(R.string.filter_adverts_last_three_day),
            resources.getString(R.string.filter_adverts_last_one_week),
            resources.getString(R.string.filter_adverts_last_one_month),
            resources.getString(R.string.filter_adverts_last_three_month)
        )

        binding.apply {

            if (viewModel.filter.maxYear.toString().isEmpty() && viewModel.filter.minYear.toString().isNotEmpty()) {
                val tempText = "${viewModel.filter. minYear} ${resources.getString(R.string.and_above)}"
                filterFragmentYearTextView.text = tempText
            }
            if (viewModel.filter.maxYear.toString().isNotEmpty() && viewModel.filter.minYear.toString().isEmpty()) {
                val tempText = "${viewModel.filter.maxYear}  ${resources.getString(R.string.and_below)}"
                filterFragmentYearTextView.text = tempText
            }
            if ((viewModel.filter.maxYear.toString().isEmpty() && viewModel.filter.minYear.toString().isEmpty())
                || (viewModel.filter.maxYear.toString() == DEFAULT_MAX_YEAR.toString() && viewModel.filter.minYear.toString() == DEFAULT_MIN_YEAR.toString())) {
                filterFragmentYearTextView.text = resources.getString(R.string.filter_adverts_all)
            }
            if ((viewModel.filter.maxYear.toString().isNotEmpty() && viewModel.filter.minYear.toString().isNotEmpty())
                && (viewModel.filter.maxYear.toString() != DEFAULT_MAX_YEAR.toString() && viewModel.filter.minYear.toString() != DEFAULT_MIN_YEAR.toString())) {
                val tempText = "${viewModel.filter.minYear}-${viewModel.filter.maxYear}"
                filterFragmentYearTextView.text = tempText
            }

            filterFragmentDateTextView.text = filterDateItems[checkedItem]

            filterFragmentYearLayout.setOnClickListener {

                val customLayout = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
                val minYearEditText = customLayout.findViewById<EditText>(R.id.min_year_edit_text)
                val maxYearEditText = customLayout.findViewById<EditText>(R.id.max_year_edit_text)
                var tempText: String

                MaterialDialog.createDialog(requireContext()) {
                    title(R.string.year)
                    view(customLayout)
                    positiveButton(R.string.okay) {
                        if (maxYearEditText.text.toString() == "" && minYearEditText.text.toString() != "") {
                            minYearQuery = minYearEditText.text.toString().toInt()
                            maxYearQuery = DEFAULT_MAX_YEAR
                            tempText =
                                "${minYearEditText.text} ${resources.getString(R.string.and_above)}"
                            filterFragmentYearTextView.text = tempText
                        }
                        if (maxYearEditText.text.toString() != "" && minYearEditText.text.toString() == "") {
                            minYearQuery = DEFAULT_MIN_YEAR
                            maxYearQuery = maxYearEditText.text.toString().toInt()
                            tempText =
                                "${maxYearEditText.text}  ${resources.getString(R.string.and_below)}"
                            filterFragmentYearTextView.text = tempText
                        }
                        if (maxYearEditText.text.toString() == "" && minYearEditText.text.toString() == "") {
                            minYearQuery = DEFAULT_MIN_YEAR
                            maxYearQuery = DEFAULT_MAX_YEAR
                            filterFragmentYearTextView.text =
                                resources.getString(R.string.filter_adverts_all)
                        }
                        if (maxYearEditText.text.toString() != "" && minYearEditText.text.toString() != "") {
                            minYearQuery = minYearEditText.text.toString().toInt()
                            maxYearQuery = maxYearEditText.text.toString().toInt()
                            tempText = "${minYearEditText.text}-${maxYearEditText.text}"
                            filterFragmentYearTextView.text = tempText
                        }

                    }
                }.show()
            }

            filterFragmentDateLayout.setOnClickListener {

                MaterialDialog.createDialog(requireContext()) {
                    title(R.string.advert_date)
                    singleChoiceItems(filterDateItems, checkedItem) {
                        checkedItem = it

                    }
                    positiveButton(R.string.okay) {
                        filterFragmentDateTextView.text = filterDateItems[checkedItem]
                        if (checkedItem == 0) {
                            minDateQuery = DEFAULT_MIN_DATE
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                        if (checkedItem == 1) {
                            minDateQuery = getDaysAgo(1)
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                        if (checkedItem == 2) {
                            minDateQuery = getDaysAgo(3)
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                        if (checkedItem == 3) {
                            minDateQuery = getDaysAgo(7)
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                        if (checkedItem == 4) {
                            minDateQuery = getDaysAgo(30)
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                        if (checkedItem == 5) {
                            minDateQuery = getDaysAgo(90)
                            maxDateQuery = DEFAULT_MAX_DATE
                        }
                    }
                }.show()
            }

            filterFragmentApplyFilterButton.setOnClickListener {
                val filter = Filter(minDateQuery, maxDateQuery, minYearQuery, maxYearQuery)
                onItemSelectListener?.invoke(filter)
                dialog?.dismiss()
            }
        }
    }

    private fun getDaysAgo(daysAgo: Int): String {
        val currentDate = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        currentDate.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return formatter.format(currentDate.time)
    }
}
package com.arabam.android.assigment.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.model.Sort
import com.arabam.android.assigment.databinding.FragmentSortBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBottomSheetBinding? = null
    private val binding get() = _binding!!
    var onItemSelectListener: ((Sort?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sort_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSortBottomSheetBinding.bind(view)

        binding.apply {

            sortFragmentPriceCheapLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_PRICE, SORT_DIRECTION_ASCENDING))
                dialog?.dismiss()
            }
            sortFragmentPriceExpensiveLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_PRICE, SORT_DIRECTION_DESCENDING))
                dialog?.dismiss()
            }
            sortFragmentAdvertDateOldLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_DATE, SORT_DIRECTION_DESCENDING))
                dialog?.dismiss()
            }
            sortFragmentAdvertDateNewLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_DATE, SORT_DIRECTION_ASCENDING))
                dialog?.dismiss()
            }
            sortFragmentModelDateOldLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_YEAR, SORT_DIRECTION_DESCENDING))
                dialog?.dismiss()
            }
            sortFragmentModelDateNewLayout.setOnClickListener {
                onItemSelectListener?.invoke(Sort(SORT_TYPE_YEAR, SORT_DIRECTION_ASCENDING))
                dialog?.dismiss()
            }
        }
    }

    companion object {
        private const val SORT_DIRECTION_ASCENDING = 0
        private const val SORT_DIRECTION_DESCENDING = 1
        private const val SORT_TYPE_PRICE = 0
        private const val SORT_TYPE_DATE = 1
        private const val SORT_TYPE_YEAR = 2
    }
}
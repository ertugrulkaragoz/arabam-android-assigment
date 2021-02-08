package com.arabam.android.assigment.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.model.CarDetail
import com.arabam.android.assigment.databinding.FragmentAdvertPropertiesBinding
import com.arabam.android.assigment.util.show

class AdvertPropertiesFragment : Fragment(R.layout.fragment_advert_properties) {

    private var _binding: FragmentAdvertPropertiesBinding? = null
    private val binding get() = _binding!!

    var carDetail: CarDetail? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAdvertPropertiesBinding.bind(view)

        binding.apply {
            if (carDetail != null) {
                if (carDetail!!.priceFormatted.isNotEmpty()) {
                    propertiesFragmentPriceTextView.text = carDetail!!.priceFormatted
                    propertiesFragmentPriceLayout.show()
                    propertiesFragmentPriceDivider.show()
                }
                if (carDetail!!.dateFormatted.isNotEmpty()) {
                    propertiesFragmentDateTextView.text = carDetail!!.dateFormatted
                    propertiesFragmentDateLayout.show()
                    propertiesFragmentDateDivider.show()
                }
                if (carDetail!!.modelName.isNotEmpty()) {
                    propertiesFragmentModelTextView.text = carDetail!!.modelName
                    propertiesFragmentModelLayout.show()
                    propertiesFragmentModelDivider.show()
                }
                if (carDetail!!.properties[2].value.toString().isNotEmpty()) {
                    propertiesFragmentYearTextView.text = carDetail!!.properties[2].value.toString()
                    propertiesFragmentYearLayout.show()
                    propertiesFragmentYearDivider.show()
                }
                if (carDetail!!.properties[3].value.toString().isNotEmpty()) {
                    propertiesFragmentGearTextView.text = carDetail!!.properties[3].value.toString()
                    propertiesFragmentGearTypeLayout.show()
                    propertiesFragmentGearTypeDivider.show()
                }
                if (carDetail!!.properties[4].value.toString().isNotEmpty()) {
                    propertiesFragmentFuelTextView.text = carDetail!!.properties[4].value.toString()
                    propertiesFragmentFuelTypeLayout.show()
                    propertiesFragmentFuelTypeDivider.show()
                }
                if (carDetail!!.properties[1].value.toString().isNotEmpty()) {
                    propertiesFragmentColorTextView.text = carDetail!!.properties[1].value.toString()
                    propertiesFragmentColorLayout.show()
                    propertiesFragmentColorDivider.show()
                }
                if (carDetail!!.properties[0].value.toString().isNotEmpty()) {
                    propertiesFragmentKilometerTextView.text = carDetail!!.properties[0].value.toString()
                    propertiesFragmentKilometerLayout.show()
                    propertiesFragmentKilometerDivider.show()
                }
            }
        }
    }
}
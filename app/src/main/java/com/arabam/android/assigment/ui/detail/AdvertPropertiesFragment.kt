package com.arabam.android.assigment.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.model.CarDetail
import com.arabam.android.assigment.data.model.CarDetailEntity
import com.arabam.android.assigment.databinding.FragmentAdvertPropertiesBinding
import com.arabam.android.assigment.util.show

class AdvertPropertiesFragment : Fragment(R.layout.fragment_advert_properties) {

    private var _binding: FragmentAdvertPropertiesBinding? = null
    private val binding get() = _binding!!

    var carDetailEntity: CarDetailEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAdvertPropertiesBinding.bind(view)

        binding.apply {
            if (carDetailEntity != null) {
                if (carDetailEntity!!.priceFormatted?.isNotEmpty()!!) {
                    propertiesFragmentPriceTextView.text = carDetailEntity!!.priceFormatted
                    propertiesFragmentPriceLayout.show()
                    propertiesFragmentPriceDivider.show()
                }
                if (carDetailEntity!!.dateFormatted?.isNotEmpty()!!) {
                    propertiesFragmentDateTextView.text = carDetailEntity!!.dateFormatted
                    propertiesFragmentDateLayout.show()
                    propertiesFragmentDateDivider.show()
                }
                if (carDetailEntity!!.modelName?.isNotEmpty()!!) {
                    propertiesFragmentModelTextView.text = carDetailEntity!!.modelName
                    propertiesFragmentModelLayout.show()
                    propertiesFragmentModelDivider.show()
                }
                if (carDetailEntity!!.properties?.get(2)?.value.toString().isNotEmpty()) {
                    propertiesFragmentYearTextView.text = carDetailEntity!!.properties?.get(2)?.value.toString()
                    propertiesFragmentYearLayout.show()
                    propertiesFragmentYearDivider.show()
                }
                if (carDetailEntity!!.properties?.get(3)?.value.toString().isNotEmpty()) {
                    propertiesFragmentGearTextView.text = carDetailEntity!!.properties?.get(3)?.value.toString()
                    propertiesFragmentGearTypeLayout.show()
                    propertiesFragmentGearTypeDivider.show()
                }
                if (carDetailEntity!!.properties?.get(4)?.value.toString().isNotEmpty()) {
                    propertiesFragmentFuelTextView.text = carDetailEntity!!.properties?.get(4)?.value.toString()
                    propertiesFragmentFuelTypeLayout.show()
                    propertiesFragmentFuelTypeDivider.show()
                }
                if (carDetailEntity!!.properties?.get(1)?.value.toString().isNotEmpty()) {
                    propertiesFragmentColorTextView.text = carDetailEntity!!.properties?.get(1)?.value.toString()
                    propertiesFragmentColorLayout.show()
                    propertiesFragmentColorDivider.show()
                }
                if (carDetailEntity!!.properties?.get(0)?.value.toString().isNotEmpty()) {
                    propertiesFragmentKilometerTextView.text = carDetailEntity!!.properties?.get(0)?.value.toString()
                    propertiesFragmentKilometerLayout.show()
                    propertiesFragmentKilometerDivider.show()
                }
            }
        }
    }
}
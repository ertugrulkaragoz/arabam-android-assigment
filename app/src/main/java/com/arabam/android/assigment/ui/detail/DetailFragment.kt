package com.arabam.android.assigment.ui.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.arabam.android.assigment.R
import com.arabam.android.assigment.api.CarApi.Companion.BASE_URL
import com.arabam.android.assigment.data.ResultWrapper
import com.arabam.android.assigment.data.model.CarDetail
import com.arabam.android.assigment.databinding.FragmentDetailBinding
import com.arabam.android.assigment.util.dismiss
import com.arabam.android.assigment.util.show
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var carDetail: CarDetail
    private lateinit var pageString: String

    private val advertPropertiesFragment = AdvertPropertiesFragment()
    private val explanationFragment = ExplanationFragment()
    private val userInfoFragment = UserInfoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(requireActivity().findViewById(R.id.detail_fragment_toolbar))
        val actionbar = (activity as AppCompatActivity).supportActionBar

        if (actionbar != null) {
            actionbar.title = resources.getString(R.string.detail_fragment_toolbar)
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.getCarDetail(args.id)
        viewModel.carDetail.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultWrapper.Success -> {
                    carDetail = it.value
                    init()
                }
                is ResultWrapper.GenericError -> {
                    Toast.makeText(
                        requireContext(),
                        it.error?.errors?.values?.first(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResultWrapper.NetworkError -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.network_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun init() {
        val sliderAdapter = ImageSlideViewPagerAdapter(requireContext(), carDetail.photos)

        advertPropertiesFragment.carDetail = carDetail
        explanationFragment.advertExplanations = carDetail.text
        userInfoFragment.userInfo = carDetail.userInfo

        replaceFragment(advertPropertiesFragment)

        binding.apply {
            detailFragmentProgressBar.dismiss()
            detailFragmentPageLayout.show()
            detailFragmentScrollView.show()
            detailFragmentTitle.show()
            detailFragmentBottomLayout.show()

            detailFragmentSlash.text = "/"
            pageString = "1/${carDetail.photos.size}"
            sliderPageTextView.text = pageString
            detailFragmentImageViewPager.adapter = sliderAdapter
            detailFragmentTitle.text = carDetail.title
            detailFragmentUserName.text = carDetail.userInfo.nameSurname
            detailFragmentCategory.text = carDetail.category.name
            detailFragmentCityName.text = carDetail.location.cityName
            detailFragmentTownName.text = carDetail.location.townName

            detailFragmentImageViewPager.addOnPageChangeListener(object :
                ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    pageString = "${position + 1}/${carDetail.photos.size}"
                    sliderPageTextView.text = pageString
                }
            })

            detailFragmentCallUserButton.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:${carDetail.userInfo.phoneFormatted}")
                startActivity(dialIntent)
            }

            detailFragmentWpIcon.setOnClickListener {
                val url =
                    "https://api.whatsapp.com/send?phone=${carDetail.userInfo.phone}&text=${resources.getString(
                        R.string.wp_text
                    )}\n${BASE_URL}api/v1/detail?id=${carDetail.id}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                if (isWhatsAppInstalled()) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.wp_not_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            detailFragmentAdvertDetailsButton.setOnClickListener {
                replaceFragment(advertPropertiesFragment)
                editButtonColors(
                    detailFragmentAdvertDetailsButton,
                    detailFragmentAdvertExplanationsButton,
                    detailFragmentUserInfoButton
                )
            }

            detailFragmentAdvertExplanationsButton.setOnClickListener {
                replaceFragment(explanationFragment)
                editButtonColors(
                    detailFragmentAdvertExplanationsButton,
                    detailFragmentAdvertDetailsButton,
                    detailFragmentUserInfoButton
                )
            }

            detailFragmentUserInfoButton.setOnClickListener {
                replaceFragment(userInfoFragment)
                editButtonColors(
                    detailFragmentUserInfoButton,
                    detailFragmentAdvertExplanationsButton,
                    detailFragmentAdvertDetailsButton
                )
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.detail_fragment_container, fragment)?.commit()
    }

    private fun editButtonColors(
        firstButton: MaterialButton,
        secondButton: MaterialButton,
        thirdButton: MaterialButton
    ) {
        val purple = ContextCompat.getColor(requireContext(), R.color.purple_500)
        val gray = ContextCompat.getColor(requireContext(), R.color.lightGray)
        val colorDivider = ContextCompat.getColor(requireContext(), R.color.colorDivider)
        val white = ContextCompat.getColor(requireContext(), R.color.white)

        firstButton.backgroundTintList = ColorStateList.valueOf(white)
        secondButton.backgroundTintList = ColorStateList.valueOf(gray)
        thirdButton.backgroundTintList = ColorStateList.valueOf(gray)

        firstButton.strokeColor = ColorStateList.valueOf(purple)
        secondButton.strokeColor = ColorStateList.valueOf(colorDivider)
        thirdButton.strokeColor = ColorStateList.valueOf(colorDivider)
    }

    private fun isWhatsAppInstalled(): Boolean {
        val pm = requireActivity().packageManager
        return try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_detailFragment_to_listFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
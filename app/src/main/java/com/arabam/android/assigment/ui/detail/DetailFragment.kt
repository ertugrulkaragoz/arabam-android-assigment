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
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.remote.CarApi.Companion.BASE_URL
import com.arabam.android.assigment.data.model.CarDetailEntity
import com.arabam.android.assigment.databinding.FragmentDetailBinding
import com.arabam.android.assigment.util.*
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var carDetailEntity: CarDetailEntity
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
        viewModel.setId(args.id)
            viewModel.carDetail.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Success -> {
                        carDetailEntity = it.data!!
                        init()
                    }
                    is Resource.Loading -> {
                        binding.apply {
                            detailFragmentProgressBar.show()
                            detailFragmentPageLayout.dismiss()
                            detailFragmentScrollView.dismiss()
                            detailFragmentTitle.dismiss()
                            detailFragmentBottomLayout.dismiss()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            it.error.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun init() {
        val sliderAdapter =
            carDetailEntity.photos?.let { ImageSlideViewPagerAdapter(requireContext(), it) }

        advertPropertiesFragment.carDetailEntity = carDetailEntity
        explanationFragment.advertExplanations = carDetailEntity.text
        userInfoFragment.userInfo = carDetailEntity.toUserInfo()

        replaceFragment(advertPropertiesFragment)

        binding.apply {
            detailFragmentProgressBar.dismiss()
            detailFragmentPageLayout.show()
            detailFragmentScrollView.show()
            detailFragmentTitle.show()
            detailFragmentBottomLayout.show()

            detailFragmentSlash.text = "/"
            pageString = "1/${carDetailEntity.photos?.size}"
            sliderPageTextView.text = pageString
            detailFragmentImageViewPager.adapter = sliderAdapter
            detailFragmentTitle.text = carDetailEntity.title
            detailFragmentUserName.text = carDetailEntity.nameSurname
            detailFragmentCategory.text = carDetailEntity.categoryName
            detailFragmentCityName.text = carDetailEntity.cityName
            detailFragmentTownName.text = carDetailEntity.townName

            detailFragmentImageViewPager.addOnPageChangeListener(object :
                ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    pageString = "${position + 1}/${carDetailEntity.photos?.size}"
                    sliderPageTextView.text = pageString
                }
            })

            detailFragmentCallUserButton.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:${carDetailEntity.phoneFormatted}")
                startActivity(dialIntent)
            }

            detailFragmentWpIcon.setOnClickListener {
                val url =
                    "https://api.whatsapp.com/send?phone=${carDetailEntity.phone}&text=${resources.getString(
                        R.string.wp_text
                    )}\n${BASE_URL}api/v1/detail?id=${carDetailEntity.carId}"
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
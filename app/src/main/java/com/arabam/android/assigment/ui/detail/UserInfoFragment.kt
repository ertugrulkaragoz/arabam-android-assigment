package com.arabam.android.assigment.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.arabam.android.assigment.R
import com.arabam.android.assigment.util.show
import com.arabam.android.assigment.data.model.UserInfo
import com.arabam.android.assigment.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    var userInfo: UserInfo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUserInfoBinding.bind(view)

        binding.apply {
            if (userInfo != null) {
                if (userInfo!!.id.toString().isNotEmpty()) {
                    userFragmentIdTextView.text = userInfo!!.id.toString()
                    userFragmentIdLayout.show()
                    userFragmentIdDivider.show()
                }
                if (userInfo!!.nameSurname.isNotEmpty()) {
                    userFragmentNameTextView.text = userInfo!!.nameSurname
                    userFragmentNameLayout.show()
                    userFragmentNameDivider.show()
                }
                if (userInfo!!.phoneFormatted.isNotEmpty()) {
                    userFragmentPhoneNumberTextView.text = userInfo!!.phoneFormatted
                    userFragmentPhoneNumberLayout.show()
                    userFragmentPhoneNumberDivider.show()
                }
            }
        }
    }
}
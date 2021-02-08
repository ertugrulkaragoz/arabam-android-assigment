package com.arabam.android.assigment.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.text.HtmlCompat
import com.arabam.android.assigment.R
import com.arabam.android.assigment.databinding.FragmentExplanationBinding

class ExplanationFragment : Fragment(R.layout.fragment_explanation) {

    private var _binding: FragmentExplanationBinding? = null
    private val binding get() = _binding!!

    var advertExplanations: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentExplanationBinding.bind(view)

        binding.apply {
            explanationFragmentTextView.text =
                advertExplanations?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        }
    }
}
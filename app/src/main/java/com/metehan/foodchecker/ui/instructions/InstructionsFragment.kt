package com.metehan.foodchecker.ui.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.metehan.foodchecker.R
import com.metehan.foodchecker.databinding.FragmentIngredientsBinding
import com.metehan.foodchecker.databinding.FragmentInstructionsBinding
import com.metehan.foodchecker.models.Result
import com.metehan.foodchecker.util.Constants
import com.metehan.foodchecker.util.Constants.Companion.RECIPE_RESULT_KEY

class InstructionsFragment : Fragment() {
    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        binding.instructionsWebView.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(websiteUrl)

        return binding.root
    }
}
package com.metehan.foodchecker.ui.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.metehan.foodchecker.R
import com.metehan.foodchecker.adapters.IngredientsAdapter
import com.metehan.foodchecker.databinding.FragmentIngredientsBinding
import com.metehan.foodchecker.databinding.FragmentRecipesBinding
import com.metehan.foodchecker.models.Result
import com.metehan.foodchecker.util.Constants.Companion.RECIPE_RESULT_KEY


class IngredientsFragment : Fragment() {

    // by lazy -> değer sadece ilk kez kullanılmaya ihtiyaç duyulduğunda initialize edilir.
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        println("URL: " + myBundle?.sourceUrl)
        setupRecyclerView(binding.root)
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return binding.root
    }

    private fun setupRecyclerView(view: View){
        binding.ingredientsRecyclerview.adapter = mAdapter
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}
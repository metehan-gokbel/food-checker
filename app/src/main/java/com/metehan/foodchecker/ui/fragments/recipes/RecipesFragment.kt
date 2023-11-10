package com.metehan.foodchecker.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.metehan.foodchecker.viewmodels.MainViewModel
import com.metehan.foodchecker.R
import com.metehan.foodchecker.adapters.RecipesAdapter
import com.metehan.foodchecker.databinding.FragmentRecipesBinding
import com.metehan.foodchecker.util.NetworkListener
import com.metehan.foodchecker.util.NetworkResult
import com.metehan.foodchecker.util.observeOnce
import com.metehan.foodchecker.viewmodels.RecipesViewModel
import com.metehan.foodchecker.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener{

    companion object {
        private const val TAG = "RecipesFragment"
    }

    // Navigation graph'daki argument'e erişmek için.
    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var networkListener: NetworkListener
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var userViewModel: AuthViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val header = binding.navView.getHeaderView(0)
        val emailTextView = header.findViewById<TextView>(R.id.email_text_view)

        userViewModel.readUser.observe(viewLifecycleOwner) {
            emailTextView.text = it.email
        }

        setupRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        networkListener = NetworkListener()
        lifecycleScope.launch {
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d(TAG, "onCreateView: ${status.toString()}")
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }

        }
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }


    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())

        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_logout -> {
                userViewModel.logOut()
                userViewModel.saveRememberMe(false)
                requireActivity().finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
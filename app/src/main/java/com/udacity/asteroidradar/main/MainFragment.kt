package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val application = requireNotNull(this.activity).application
        ViewModelProvider(
            this, MainViewModelFactory(application)
        ).get(MainViewModel::class.java)
    }

    lateinit var adapter: AsteroidListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        // initialize the adapter of the recyclerview
        adapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener{
            viewModel.navigateToDetailFragment(it)
        })
        binding.asteroidRecycler.adapter = adapter
        // set an observer over Asteroids to submitList
        viewModel.weekAsteroids.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
        // navigate to detail fragment
        viewModel.asteroid.observe(viewLifecycleOwner) {
            it?.let {
               this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                // reset Asteroid
                viewModel.navigateToDetailFragmentComplete()
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_saved_asteroids_menu -> {
                viewModel.allAsteroids.observe(viewLifecycleOwner) {
                    it?.let {
                        adapter.submitList(it)
                    }
                }
            }
            R.id.show_week_asteroids_menu -> {
                viewModel.weekAsteroids.observe(viewLifecycleOwner) {
                    it?.let {
                        adapter.submitList(it)
                    }
                }
            }
            else -> {
                viewModel.todayAsteroids.observe(viewLifecycleOwner) {
                    it?.let {
                        adapter.submitList(it)
                    }
                }
            }
        }
        return true
    }
}

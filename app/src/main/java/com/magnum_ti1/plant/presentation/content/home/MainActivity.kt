package com.magnum_ti1.plant.presentation.content.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.*
import com.magnum_ti1.plant.data.local.preferences.UserPreferences
import com.magnum_ti1.plant.databinding.ActivityMainBinding
import com.magnum_ti1.plant.presentation.content.detail.DetailActivity
import com.magnum_ti1.plant.presentation.content.profile.ProfileActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by inject()
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(::onClickPlant)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            imgProfile.setOnClickListener(this@MainActivity)
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding.apply {
            rvPlantList.setHasFixedSize(true)
            rvPlantList.layoutManager = LinearLayoutManager(this@MainActivity)
            rvPlantList.adapter = mainAdapter
        }
    }

    private fun onClickPlant(plantId: Int?) {
        launchActivity<DetailActivity> {
            putExtra(EXTRA_DATA, plantId)
        }
    }

    private fun getPlantListObserver() {
        mainViewModel.getPlantList().observe(this) {
            when (it) {
                is Response.Loading -> binding.loading.root.isVisible = true
                is Response.Success -> {
                    binding.loading.root.isVisible = false
                    mainAdapter.submitList(it.data)
                }
                is Response.Fail -> {
                    binding.loading.root.isVisible = false
                    it.e.message?.showMessage(this)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.imgProfile.id -> launchActivity<ProfileActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        getPlantListObserver()
    }

    override fun onResume() {
        super.onResume()
        val userName = UserPreferences.initPreferences(this).userInfo.username
        val photo = UserPreferences.initPreferences(this).userInfo.photo
        binding.apply {
            tvUsername.text = userName
            if (photo.isNullOrBlank()) {
                imgProfile.setImageResource(R.drawable.sample_avatar)
            } else {
                imgProfile.loadCircleImageUrl(photo)
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
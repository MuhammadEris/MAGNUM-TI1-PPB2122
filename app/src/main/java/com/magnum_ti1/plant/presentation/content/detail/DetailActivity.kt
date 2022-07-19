package com.magnum_ti1.plant.presentation.content.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.magnum_ti1.plant.common.Response
import com.magnum_ti1.plant.common.loadImagePlantUrl
import com.magnum_ti1.plant.common.showMessage
import com.magnum_ti1.plant.data.entity.PlantDetail
import com.magnum_ti1.plant.databinding.ActivityDetailBinding
import com.magnum_ti1.plant.presentation.content.home.MainActivity.Companion.EXTRA_DATA
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by inject()
    private lateinit var plantDetail: PlantDetail
    private var plantId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewActionBack.setOnClickListener(this@DetailActivity)
        }

        plantId = intent.getIntExtra(EXTRA_DATA, 0)
        getDetailPlantListObserver(plantId)
    }

    private fun getDetailPlantListObserver(plantId: Int?) {
        detailViewModel.getDetailPlantList(plantId).observe(this) {
            when (it) {
                is Response.Loading -> binding.loading.root.isVisible = true
                is Response.Success -> {
                    binding.loading.root.isVisible = false
                    getData(it.data)
                }
                is Response.Fail -> {
                    binding.loading.root.isVisible = false
                    it.e.message?.showMessage(this)
                }
            }
        }
    }

    private fun getData(data: List<PlantDetail>) {
        binding.apply {
            data.forEach { plantDetails ->
                plantDetail = plantDetails
                imgPlant.loadImagePlantUrl(plantDetails.image_url)
                tvPlantName.text = plantDetails.name
                tvPlantDescription.text = plantDetails.description
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.viewActionBack.id -> onBackPressed()
        }
    }
}
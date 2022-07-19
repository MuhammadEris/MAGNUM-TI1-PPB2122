package com.magnum_ti1.plant.presentation.content.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.common.customToolbar
import com.magnum_ti1.plant.common.hideSoftInput
import com.magnum_ti1.plant.common.showMessage
import com.magnum_ti1.plant.data.local.db.DatabaseHelper
import com.magnum_ti1.plant.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var dbHelper: DatabaseHelper
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        customToolbar(
            "",
            binding.toolbar.root,
            true
        )

        binding.apply {
            btnSave.setOnClickListener(this@EditProfileActivity)
            toolbar.tvDelete.setOnClickListener(this@EditProfileActivity)
        }

        getData()
        setState()
    }

    private fun getData() {
        val res = dbHelper.readData("1")
        val buffer = StringBuffer()
        while (res.moveToNext()) {
            buffer.append(res.getString(1))
        }
        if (buffer.toString().isNotEmpty()) {
            binding.edtBio.setText(buffer.toString())
            isEdit = true
        }
    }

    private fun setState() {
        val btnTitle: String = if (isEdit) {
            getString(R.string.update)
        } else {
            getString(R.string.save)
        }

        binding.btnSave.text = btnTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.toolbar.tvDelete.id -> {
                try {
                    val res = dbHelper.readData("1")
                    val buffer = StringBuffer()
                    while (res.moveToNext()) {
                        buffer.append(res.getString(1))
                    }
                    if (buffer.toString().isNotEmpty()) {
                        dbHelper.deleteData("1")
                        binding.edtBio.setText("")
                        getString(R.string.bio_successfully_deleted).showMessage(this)
                        finish()
                    }
                } catch (e: Exception) {
                    e.message?.showMessage(this)
                }
            }
            binding.btnSave.id -> {
                if (isEdit) {
                    try {
                        val isUpdate =
                            dbHelper.updateData("1", binding.edtBio.text.toString().trim())
                        if (isUpdate) {
                            binding.edtBio.clearFocus()
                            hideSoftInput(binding.edtBio)
                            getString(R.string.bio_successfully_updated).showMessage(this)
                        } else {
                            binding.edtBio.clearFocus()
                            getString(R.string.bio_failed_updated).showMessage(this)
                        }
                    } catch (e: Exception) {
                        e.message?.showMessage(this)
                    }
                } else {
                    try {
                        dbHelper.createData(binding.edtBio.text.toString())
                        binding.edtBio.clearFocus()
                        hideSoftInput(binding.edtBio)
                        getString(R.string.bio_successfully_created).showMessage(this)
                    } catch (e: Exception) {
                        e.message?.showMessage(this)
                    }
                }
            }
        }
    }
}
package com.magnum_ti1.plant.presentation.broadcast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magnum_ti1.plant.R
import com.magnum_ti1.plant.databinding.ActivitySmsReceiverBinding

class SmsReceiverActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsReceiverBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Incoming Message"
        val senderNo = intent.getStringExtra(EXTRA_SMS_NO)
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MESSAGE)

        if (senderMessage == getString(R.string.successfully_message)) {
            binding.apply {
                tvComingFrom.text = getString(R.string.coming_from, senderNo)
                tvMessage.text = senderMessage
                btnOk.setOnClickListener {
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_message"
    }
}
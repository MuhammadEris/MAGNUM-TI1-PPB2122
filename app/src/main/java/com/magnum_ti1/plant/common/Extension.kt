package com.magnum_ti1.plant.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.magnum_ti1.plant.R

fun AppCompatActivity.customToolbar(
    toolbarTitle: String,
    toolbar: Toolbar?,
    navigationIcon: Boolean
) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        title = toolbarTitle
        setDisplayHomeAsUpEnabled(navigationIcon)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    option: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, option)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)

fun Any.showMessage(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(context, this.toString(), duration).apply {
        show()
    }

fun EditText.convertToString() = this.text.toString().trim()

fun String.invalidEmail(): Boolean =
    !Patterns.EMAIL_ADDRESS.matcher(this).matches() && isNotEmpty()

fun ImageView.loadCircleImageUrl(image: String) {
    Glide.with(this.context).load(image).circleCrop().into(this)
}

fun ImageView.loadImagePlantUrl(image: String?) {
    Glide.with(this.context).load(image).placeholder(R.drawable.ic_baseline_refresh_24)
        .error(R.drawable.ic_baseline_broken_image_24).into(this)
}

fun AppCompatActivity.replaceFragment(resource: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .replace(resource, fragment)
        .commit()
}

fun Context.hideSoftInput(view: View) {
    val im = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}
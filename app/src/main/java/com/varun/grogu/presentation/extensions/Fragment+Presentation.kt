package com.varun.grogu.presentation.extensions

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.varun.grogu.R

fun <T : ViewDataBinding> Fragment.getDataBinding(layout: Int, container: ViewGroup?): T {
    val binding: T = DataBindingUtil.inflate(layoutInflater, layout, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    return binding
}

/**
 * Shows a indefinite [Snackbar] with a retry action.
 *
 * @param message: Text to show on the snackbar.
 * @param onClick: Block to run when retry gets clicked.
 */
fun Fragment.showSnackBarWithRetry(message: Int, onClick: (() -> Unit)) {
    val snackBar = Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
    snackBar.setAction(R.string.retry) {
        onClick()
        snackBar.dismiss()
    }
    snackBar.show()
}

fun Fragment.showSnackBar(message: Int, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(requireActivity().findViewById(android.R.id.content), message, duration).show()
}

fun Fragment.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(requireActivity().findViewById(android.R.id.content), message, duration).show()
}
package com.varun.grogu.presentation.extensions

import android.view.View
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Sets the visibility of [ShimmerFrameLayout] to [View.GONE] and stops the animation.
 */
fun ShimmerFrameLayout.hideAndStopEffect() {
    stopShimmer()
    isVisible = false
}

/**
 * Sets the visibility of [ShimmerFrameLayout] to [View.VISIBLE] and starts the animation.
 */
fun ShimmerFrameLayout.showAndStartEffect() {
    startShimmer()
    isVisible = true
}
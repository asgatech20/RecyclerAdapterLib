package com.asga.recycler_adapter.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.asga.recycler_adapter.AsgaRecyclerView


/**
 * Update the
 */
@BindingAdapter("setLoadingStatus")
fun setLoadingStatus(view: AsgaRecyclerView, Loading: Boolean) {
    if (Loading)
        view.getLoadingContainer().visibility = View.GONE
    else
        view.getLoadingContainer().visibility = View.VISIBLE

}

@BindingAdapter("showEmpty")
fun setShowEmpty(view: AsgaRecyclerView, showEmpty: Boolean) {
    if (view.getEmptyViewRes() == -1) return
    if (showEmpty)
        view.getEmptyViewContainer().visibility = View.VISIBLE
    else
        view.getEmptyViewContainer().visibility = View.GONE
}

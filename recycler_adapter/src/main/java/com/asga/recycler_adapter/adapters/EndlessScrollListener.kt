package com.asga.recycler_adapter.adapters

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class EndlessScrollListener(layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {
    private var mLinearLayoutManager: LinearLayoutManager? = layoutManager as LinearLayoutManager

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 1

    // The current offset position of data you have loaded
    private var currentPage = 0

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last reset of data to load.
    private var loading = true

    // Sets the starting page position
    private val startingPageIndex = 1
    private fun setVisibleThreshold(layoutManager: GridLayoutManager) {
        visibleThreshold *= layoutManager.spanCount
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        if (mLinearLayoutManager == null) return
        handlePagination(view)
    }

    private fun handlePagination(view: RecyclerView) {
        val firstVisibleItem = mLinearLayoutManager!!.findFirstVisibleItemPosition()
        val visibleItemCount = view.childCount
        val totalItemCount = mLinearLayoutManager!!.itemCount

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int)

    init {
        if (mLinearLayoutManager is GridLayoutManager) setVisibleThreshold(layoutManager as GridLayoutManager)
    }
}
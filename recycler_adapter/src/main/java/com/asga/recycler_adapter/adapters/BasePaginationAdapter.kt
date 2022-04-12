package com.asga.recycler_adapter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListPopupWindow
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.BR
import com.asga.recycler_adapter.R
import com.asga.recycler_adapter.databinding.LoadRowBinding
import com.asga.recycler_adapter.view_holders.BaseViewHolder


open class BasePaginationAdapter<Binding : ViewDataBinding, DM : Any> : BaseAdapter<Binding, DM> {

    private val LOADING_ROW = 88
    private lateinit var paginationHandler: PaginationHandler<DM>
    private var loading = false
    private var currentPage = 0
    private var canLoadMore = true

    constructor(@LayoutRes rowRes: Int) : this(
        rowRes,
        BR.dataModel,
        0
    )


    constructor(
        @LayoutRes rowRes: Int,
        bindingVariable: Int,
        initialPage: Int = 0
    ) : super(rowRes = rowRes, bindingVariable = bindingVariable) {
        this.currentPage = initialPage
    }

    /**
     * Init the pagination implementation
     */
    fun setPaginationHandler(paginationHandler: PaginationHandler<DM>) {
        this.paginationHandler = paginationHandler
        if (recyclerView != null) setupPagination(recyclerView!!)
    }

    /**
     * Attach the pagination listener to the recycler
     */
    private fun setupPagination(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object :
            EndlessScrollListener(recyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int, items: Int) {
                if (loading || !canLoad()) return
                paginationHandler.onLoadMore(++currentPage, items)
                enableLoading()
            }

            /**
             * Return if adapter can load more data
             */
            private fun canLoad() = canLoadMore
        })
    }

    /**
     * Inflate recycler rows according to the type needed (when waiting for next page items then inflate loading row
     * if else then inflate the data row )
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == LOADING_ROW) BaseViewHolder<ViewDataBinding, DM>(
            getLoadingRowBinding(parent)
        )
        else super.onCreateViewHolder(parent, viewType)
    }


    /**
     * Return the paginated loading row layout and setting its width and height params according to the layout manager orientation
     */
    private fun getLoadingRowBinding(parent: ViewGroup?): ViewDataBinding {
        val binding: LoadRowBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.load_row, parent, false)
        var params: ViewGroup.LayoutParams = binding.progressContainer.getLayoutParams()
        if (isVertical())
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ListPopupWindow.WRAP_CONTENT
            ) else if (isHorizontal())
            params = ViewGroup.LayoutParams(
                ListPopupWindow.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        binding.progressContainer.layoutParams = params
        return binding
    }

    /**
     * Return item view type in both cases ( Normal case and pagination loading case)
     */
    override fun getItemViewType(position: Int): Int {
        return if (isLoadingRow(position)) LOADING_ROW else super.getItemViewType(position)
    }

    /**
     * Check if the adapter status is loading data of next page
     */
    private fun isLoadingRow(position: Int): Boolean {
        return loading && dataList != null && dataList!![position] == null
    }

    /**
     * Set the data list items to the adapter and notify the change
     */
    override fun setData(data: List<DM>) {
        hideLoading()
        super.setData(data)
    }

    /**
     * Called if you want to update the dataList of adapter and notify the change
     */
    override fun updateDataList(newData: List<DM>) {
        hideLoading()
        super.updateDataList(newData)
    }

    /**
     * show the pagination loading row
     */
    private fun enableLoading() {
        if (dataList != null && dataList!!.size > 0 && dataList!![dataList!!.size - 1] != null) {
            dataList!!.add(null)
            loading = true
            notifyItemInserted(dataList!!.size - 1)
        }
    }

    /**
     * hide the pagination loading row
     */
    private fun hideLoading() {
        if (dataList != null && dataList!!.size >= 1 && dataList!![dataList!!.size - 1] == null) {
            dataList!!.removeAt(dataList!!.size - 1)
            notifyItemRemoved(dataList!!.size)
            loading = false
        }
    }

    /**
     * set the adapter if can load more data or not
     */
    fun setCanLoad(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }

    /**
     * Pagination Interface
     * @onLoadMore got called every time recycler scroll come to the end so user can fetch next page data
     */
    interface PaginationHandler<DM> {
        fun onLoadMore(page: Int, totalRows: Int)
    }
}
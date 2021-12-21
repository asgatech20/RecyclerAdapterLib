package com.asga.recycler_adapter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListPopupWindow
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.BR
import com.asga.recycler_adapter.R
import com.asga.recycler_adapter.databinding.LoadRowBinding


open class BaseLoaderAdapter<Binding : ViewDataBinding, DM : Any> : BaseAdapter<Binding, DM> {

    private val LOADING_ROW = 88
    private lateinit var paginationHandler: PaginationHandler<DM>
    private var loading = false
    private var totalItemCount = 0
    private var currentPage = 1

    constructor(@LayoutRes rowRes: Int, paginationHandler: PaginationHandler<DM>) : this(
        rowRes,
        BR.dataModel,
        paginationHandler
    )

    constructor(
        @LayoutRes rowRes: Int,
        bindingVariable: Int,
        paginationHandler: PaginationHandler<DM>
    ) : this(rowRes, bindingVariable, paginationHandler, null)


    constructor(
        @LayoutRes rowRes: Int,
        paginationHandler: PaginationHandler<DM>,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?
    ) : this(rowRes, R.layout.layout_empty_data, BR.dataModel, paginationHandler, rowClickListener)


    constructor(
        @LayoutRes rowRes: Int,
        bindingVariable: Int,
        paginationHandler: PaginationHandler<DM>,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?
    ) :
            this(
                rowRes,
                R.layout.layout_empty_data,
                bindingVariable,
                paginationHandler,
                rowClickListener
            )


    constructor(
        @LayoutRes rowRes: Int,
        @LayoutRes emptyViewRes: Int,
        bindingVariable: Int,
        paginationHandler: PaginationHandler<DM>,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?
    ) :
            super(
                rowRes = rowRes,
                emptyViewRes = emptyViewRes,
                bindingVariable = bindingVariable,
                rowClickListener = rowClickListener,
                viewClickModels = null
            ) {
        setPaginationHandler(paginationHandler)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (paginationHandler != null) setupPagination(recyclerView)
    }

    private fun setupPagination(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object :
            EndlessScrollListener(recyclerView.layoutManager!!) {
            override fun onLoadMore(page: Int, items: Int) {
                if (isEmpty() || loading) return
                if (paginationHandler != null && items < totalItemCount) {
                    paginationHandler.onLoadMore(++currentPage, items, getLastDM()!!)
                    enableLoading()
                }
            }
        })
    }


    override fun isAdapterBusinessRow(): Boolean {
        return super.isAdapterBusinessRow() || loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == LOADING_ROW) BaseViewHolder<ViewDataBinding, DM>(
            getLoadingRowBinding(
                parent
            )
        )
        else super.onCreateViewHolder(
            parent,
            viewType
        )
    }

    private fun getLoadingRowBinding(parent: ViewGroup?): ViewDataBinding {
        val binding: LoadRowBinding =
            DataBindingUtil.inflate(layoutInflater!!, R.layout.load_row, parent, false)
        var params: ViewGroup.LayoutParams = binding.progressContainer.getLayoutParams()
        if (isVertical()) params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ListPopupWindow.WRAP_CONTENT
        ) else if (isHorizontal()) params = ViewGroup.LayoutParams(
            ListPopupWindow.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.progressContainer.setLayoutParams(params)
        return binding
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingRow(position)) LOADING_ROW else super.getItemViewType(position)
    }

    private fun isLoadingRow(position: Int): Boolean {
        return loading && dataList != null && dataList!![position] == null
    }

    fun enableLoading() {
        if (dataList != null && dataList!!.size > 0 && dataList!![dataList!!.size - 1] != null) {
            dataList!!.add(null)
            loading = true
            notifyItemInserted(dataList!!.size - 1)
        }
    }

    fun disableLoading() {
        if (dataList != null && dataList!!.size >= 1 && dataList!![dataList!!.size - 1] == null) {
            dataList!!.removeAt(dataList!!.size - 1)
            notifyItemRemoved(dataList!!.size)
            loading = false
        }
    }

    private fun setPaginationHandler(paginationHandler: PaginationHandler<DM>): BaseLoaderAdapter<Binding, DM>? {
        this.paginationHandler = paginationHandler
        if (recyclerView != null) setupPagination(recyclerView!!)
        return this
    }

    fun setTotalItemCount(totalItemCount: Int): BaseLoaderAdapter<Binding, DM>? {
        this.totalItemCount = totalItemCount
        return this
    }

    fun setTotalItemCount(observableInt: ObservableInt): BaseLoaderAdapter<Binding, DM>? {
        observableInt.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                totalItemCount = observableInt.get()
            }
        })
        return this
    }

    interface PaginationHandler<DM> {
        fun onLoadMore(page: Int, totalRows: Int, lastDM: DM)
    }
}
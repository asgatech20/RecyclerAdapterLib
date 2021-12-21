package com.asga.recycler_adapter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.asga.recycler_adapter.BR
import com.asga.recycler_adapter.R
import com.asga.recycler_adapter.databinding.LayoutEmptyDataBinding
import java.util.*

/* Created by ibrahim.ali on 10/13/2021 */
open class BaseAdapter<Binding : ViewDataBinding, DM : Any> constructor() : Adapter<ViewHolder>() {
    private val EMPTY_VIEW = 77
    private val ROW = 78

    protected open var recyclerView: RecyclerView? = null
    open var dataList: MutableList<DM?>? = null
    open var layoutInflater: LayoutInflater? = null
    private var rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>? = null
    private var disableEmptyRow = false
    private var positionChangeListener: PositionChangeListener? = null

    @LayoutRes
    private var emptyViewRes = 0

    @DrawableRes
    private var emptyViewIconRes: Int = android.R.drawable.ic_search_category_default

    @StringRes
    private var emptyViewMsg: Int = R.string.emptyData

    @StringRes
    private var emptyViewMsgDesc: Int = R.string.no_result
    private var rowRes = 0
    private var bindingVariable = 0
    private var viewClickModels: List<ViewClickModel<Binding, DM>>? = null
    private var emptyViewBinding: ViewDataBinding? = null


    constructor(@LayoutRes rowRes: Int) : this(rowRes, BR.dataModel, null)


    constructor(
        @LayoutRes rowRes: Int,
        rowCLickListener: BaseViewHolder.RowCLickListener<Binding, DM>?
    )
            : this(rowRes, BR.dataModel, rowCLickListener)


    constructor(
        @LayoutRes rowRes: Int,
        bindingVariable: Int,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?
    ) : this(rowRes, R.layout.layout_empty_data, bindingVariable, rowClickListener, null)


    constructor(
        @LayoutRes rowRes: Int,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?,
        viewClickModels: List<ViewClickModel<Binding, DM>>?
    ) :
            this(
                rowRes,
                R.layout.layout_empty_data,
                BR.dataModel,
                rowClickListener,
                viewClickModels
            )


    constructor(
        @LayoutRes rowRes: Int,
        @LayoutRes emptyViewRes: Int,
        bindingVariable: Int,
        rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>?,
        viewClickModels: List<ViewClickModel<Binding, DM>>?
    ):this() {
        this.rowRes = rowRes
        dataList = null
        this.emptyViewRes = emptyViewRes
        this.bindingVariable = bindingVariable
        this.rowClickListener = rowClickListener
        setViewClickModels(viewClickModels)
    }
    // data section
    open fun addRow(dm: DM) {
        if (dataList == null) setDataList(ArrayList<DM>())
        dataList!!.add(dm)
        notifyItemInserted(dataList!!.size)
    }

    open fun setRow(position: Int, dm: DM) {
        if (position < dataList!!.size) {

            dataList!![position] = dm
            notifyItemChanged(position)
        } else addRow(dm)
    }

    open fun remove(position: Int) {
        if (position >= itemCount) return
        dataList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position)
    }

    open fun isEmpty(): Boolean {
        return dataList == null || dataList!!.isEmpty()
    }

    open fun getLastDM(): DM? {
        return if (isEmpty()) null else dataList!![dataList!!.size - 1]
    }

    open fun clearData() {
        dataList = null
        notifyDataSetChanged()
    }
    @CallSuper
    open fun setDataList(data: List<DM>): BaseAdapter<Binding, DM>? {
        dataList = data.toMutableList()
        notifyDataSetChanged()
        return this
    }

    @CallSuper
    fun updateDataList(@NonNull newData: List<DM>): BaseAdapter<Binding, DM> {
        //in case dataList not reset yet or the recent data is null(since refers to clear old data) then reset dataList instead
        if (dataList == null || dataList!!.isEmpty())
            setDataList(newData);
        else {
            dataList!!.plus(newData);
            notifyItemRangeInserted(dataList!!.size - newData.size, dataList!!.size);
            notifyItemChanged(dataList!!.size - (newData.size + 1));
        }
        return this;
    }

    open fun getData(): List<DM?>? {
        return dataList
    }

    open operator fun get(position: Int): DM? {
        return if (dataList != null && dataList!!.size > position) dataList!![position] else null
    }

    protected open fun getDm(position: Int): DM? {
        return if (dataList == null || dataList!!.isEmpty()) null else dataList!![position]
    }
    override fun getItemCount(): Int {
        return if (dataList == null) 0 else if (showEmpty()) 1 else dataList!!.size
    }

    open fun getDataSize(): Int {
        return if (dataList == null) 0 else dataList!!.size
    }


    // end data section

    //////////empty section////////////

    protected open fun bindView(parent: ViewGroup?, rowRes: Int): Binding? {
        return if (rowRes == -1) null else DataBindingUtil.inflate(
            layoutInflater!!,
            rowRes,
            parent,
            false
        )
    }
    protected open fun getEmptyRow(parent: ViewGroup?): ViewDataBinding? {
        if (emptyViewBinding == null) emptyViewBinding = bindView(parent, emptyViewRes)
        if (emptyViewBinding is LayoutEmptyDataBinding) bindEmptyViewVariables(emptyViewBinding as LayoutEmptyDataBinding)
        return emptyViewBinding
    }

    private fun bindEmptyViewVariables(binding: LayoutEmptyDataBinding) {
        binding.desc = binding.root.context.getString(emptyViewMsgDesc)
        binding.icon = binding.root.context.getDrawable(emptyViewIconRes)
        binding.msg = binding.root.context.getString(emptyViewMsg)
    }

    open fun setEmptyView(@LayoutRes emptyViewRes: Int): BaseAdapter<Binding, DM>? {
        this.emptyViewRes = emptyViewRes
        return this
    }

    open fun setEmptyView(emptyViewBinding: ViewDataBinding?): BaseAdapter<Binding, DM>? {
        this.emptyViewBinding = emptyViewBinding
        return this
    }

    open fun setEmptyView(
        @StringRes msg: Int,
        @StringRes desc: Int,
        @DrawableRes iconRes: Int
    ): BaseAdapter<Binding, DM>? {
        emptyViewMsg = msg
        emptyViewMsgDesc = desc
        emptyViewIconRes = iconRes
        return this
    }

    protected open fun showEmpty(): Boolean {
        return !disableEmptyRow && dataList != null && dataList!!.isEmpty()
    }
    open fun disableEmptyRow(flag: Boolean): BaseAdapter<Binding, DM>? {
        disableEmptyRow = flag
        return this
    }


    ////////////////end of empty section///////////


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == EMPTY_VIEW)
            BaseViewHolder<ViewDataBinding, DM>(getEmptyRow(parent)!!)
        else
            BaseViewHolder(bindView(parent, rowRes) as Binding, rowClickListener, bindingVariable, viewClickModels)
    }



    override fun getItemViewType(position: Int): Int {
        return if (showEmpty()) EMPTY_VIEW else ROW
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is BaseViewHolder<*, *>) {
            (holder as BaseViewHolder<Binding, DM>).onBind(position, getDm(position)!!, itemCount)
        }
        newPosition(position)
    }



    open fun newPosition(position: Int) {
        if (positionChangeListener != null) positionChangeListener!!.onNewPosition(position)
    }

////////////////////////////helper//////////////////
    protected open fun isAdapterBusinessRow(): Boolean {
        return showEmpty()
    }

    protected open fun isVertical(): Boolean {
        return isOrientation(VERTICAL)
    }

    protected open fun isHorizontal(): Boolean {
        return isOrientation(HORIZONTAL)
    }
    private fun isOrientation(orientation: Int): Boolean {
        if (recyclerView!!.layoutManager is LinearLayoutManager) {
            val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager?
            return layoutManager!!.orientation == orientation
        }
        return false
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    open fun scrollTo(position: Int): BaseAdapter<Binding, DM>? {
        recyclerView!!.smoothScrollToPosition(position)
        return this
    }
    /////////////////////helper///////////////////////
//////////////listener///////////


    open fun setViewClickModels(viewClickModels: List<ViewClickModel<Binding, DM>>?):
            BaseAdapter<Binding, DM>? {
        this.viewClickModels = viewClickModels
        return this
    }

    open fun addViewClickModel(viewClickModel: ViewClickModel<Binding, DM>): BaseAdapter<Binding, DM>? {
        if (viewClickModels == null) viewClickModels = mutableListOf()
        viewClickModels = viewClickModels!!.plus(viewClickModel)
        return this
    }

    open fun addOnPositionChangeListener(positionChangeListener: PositionChangeListener):
            BaseAdapter<Binding, DM>? {
        this.positionChangeListener = positionChangeListener
        return this
    }

    open fun setRowClickListener(rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>):
            BaseAdapter<Binding, DM>? {
        this.rowClickListener = rowClickListener
        return this
    }
//////////////listener///////////

    interface PositionChangeListener {
        fun onNewPosition(position: Int)
    }

}
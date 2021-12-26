package com.asga.recycler_adapter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asga.recycler_adapter.BR
import java.util.*

open class BaseAdapter<Binding : ViewDataBinding, DM : Any> constructor() : Adapter<ViewHolder>() {

    private var baseViewHolder: BaseViewHolder<Binding, DM>? = null
    protected var recyclerView: RecyclerView? = null
    protected var dataList: MutableList<DM?>? = null
    protected var layoutInflater: LayoutInflater? = null
    private var rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>? = null
    private var disableEmptyRow = false

    @LayoutRes
    private var emptyViewRes = 0
    private var rowRes = 0
    private var bindingVariable = 0

    constructor(@LayoutRes rowRes: Int) : this(rowRes, BR.dataModel)

    constructor(
        @LayoutRes rowRes: Int,
        bindingVariable: Int,
    ) : this() {
        this.rowRes = rowRes
        dataList = null
        this.bindingVariable = bindingVariable
    }

    /**
     * Insert an item to the recycler and notify the change to the entire dataSet
     */
    fun addRow(dm: DM) {
        if (dataList == null) setData(ArrayList<DM>())
        dataList!!.add(dm)
        notifyItemInserted(dataList!!.size)
    }

    /**
     * Remove an item from the recycler and notify the change to the entire dataSet
     */
    fun remove(position: Int) {
        if (position >= itemCount) return
        dataList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position)
    }

    /**
     * Check if dataSet of the adapter is empty
     */
    fun isEmpty(): Boolean {
        return dataList == null || dataList!!.isEmpty()
    }

    /**
     * Clear all data from adapter and notify the change
     */
    fun clearData() {
        dataList = null
        notifyDataSetChanged()
    }

    /**
     * Set the data list items to the adapter and notify the change
     */
    fun setData(data: List<DM>) {
        dataList = data.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Called if you want to update the dataList of adapter
     * If there no items so it set the dataList to the @newData
     * If there is already items in the adapter so it append the data to the existed one
     */
    @CallSuper
    fun updateDataList(@NonNull newData: List<DM>): BaseAdapter<Binding, DM> {
        if (dataList == null || dataList!!.isEmpty()) // case adapter dataList has no data yet
            setData(newData);
        else { // case update current data and append the sent data to it
            dataList!!.plus(newData);
            notifyItemRangeInserted(dataList!!.size - newData.size, dataList!!.size);
            notifyItemChanged(dataList!!.size - (newData.size + 1));
        }
        return this;
    }

    /**
     * Return the current adapter data
     */
    fun getData(): List<DM?>? {
        return dataList
    }

    /**
     * Return an Item according to the sent parameter (@position)
     */
    fun getItem(position: Int): DM? {
        return if (dataList != null && dataList!!.size > position) dataList!![position] else null
    }

    /**
     * Return Item count of the adapter dataList
     */
    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList!!.size
    }


    /***********************************************************  Inflate the Row layout to the adapter list   ****************/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(parent.context)
        baseViewHolder = BaseViewHolder(
            bindView(parent, rowRes) as Binding,
            bindingVariable
        )
        return baseViewHolder as BaseViewHolder<Binding, DM>
    }

    protected open fun bindView(parent: ViewGroup?, rowRes: Int): Binding? {
        return if (rowRes == -1) null else DataBindingUtil.inflate(
            layoutInflater!!,
            rowRes,
            parent,
            false
        )
    }

    /*************************************************************************************************************************/

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is BaseViewHolder<*, *>) {
            (holder as BaseViewHolder<Binding, DM>).onBind(position, getItem(position)!!, itemCount)
        }
    }


    /********************************************************** Helpers *************************************************/

    /**
     * Smooth scroll to a certain position
     */
    fun scrollTo(position: Int): BaseAdapter<Binding, DM>? {
        recyclerView!!.smoothScrollToPosition(position)
        return this
    }

    /**
     * set click listeners to a certain view ids in the row
     */
    fun setViewClickModels(viewClickModels: List<ViewClickModel<Binding, DM>>?) {
        baseViewHolder!!.setViewClickModels(viewClickModels)
    }

    /**
     * Add a click listener to a certain view id in the row
     */
    fun addViewClickModel(viewClickModel: ViewClickModel<Binding, DM>) {
        baseViewHolder!!.addViewClickModel(viewClickModel)
    }

    /**
     * Set listener of row click
     */
    fun setRowClickListener(rowClickListener: BaseViewHolder.RowCLickListener<Binding, DM>) {
        baseViewHolder!!.setRowClickListener(rowClickListener)
    }

    /**
     * Check if the layout manager orientation is VERTICAL
     */
    protected open fun isVertical(): Boolean {
        return isValidOrientation(RecyclerView.VERTICAL)
    }

    /**
     * Check if the layout manager orientation is HORIZONTAL
     */
    protected open fun isHorizontal(): Boolean {
        return isValidOrientation(RecyclerView.HORIZONTAL)
    }

    /**
     * Validate the layout manager orientation of recycler according to the sent param
     */
    private fun isValidOrientation(orientation: Int): Boolean {
        if (recyclerView!!.layoutManager is LinearLayoutManager) {
            val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager?
            return layoutManager!!.orientation == orientation
        }
        return false
    }
    /***************************************************************************************************************************/
}
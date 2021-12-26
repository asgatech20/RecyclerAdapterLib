package com.asga.recycler_adapter.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.BR
import java.util.*

/**
 * @Author: Muhammad Noamany
 * @Date: 12/22/2021
 * @Email: muhammadnoamany@gmail.com
 */


/**
 * BaseViewHolder for using on BaseAdapters as a RecyclerView.ViewHolder
 * @Binding -> the viewBinding of recycler row layout
 * @DM -> the data class to be set and viewed in the recycler row
 */
open class BaseViewHolder<Binding : ViewDataBinding, DM : Any> : RecyclerView.ViewHolder {
    var binding: Binding? = null
    private var cLickListener: RowCLickListener<Binding, DM>? = null
    private var viewClickModels: List<ViewClickModel<Binding, DM>>? = null
    private var bindingVariable = -1
    private var dataModel: DM? = null
    private var itemCount = 0

    /****************************************** Constructors of the class **************************************/
    constructor(binding: Binding) : this(binding, -1)

    constructor(
        binding: Binding,
        bindingVariable: Int
    ) : super(binding.root) {
        this.binding = binding
        this.bindingVariable = bindingVariable
    }

    /**************************************************************************************************************/

    /**
     * Add a click listener to a certain view id in the row
     */
    fun addViewClickModel(viewClickModel: ViewClickModel<Binding, DM>?): BaseViewHolder<Binding, DM>? {
        if (viewClickModels == null) viewClickModels = ArrayList()
        viewClickModels!!.plus(viewClickModel)
        setViewCLickListeners()
        return this
    }

    /**
     * set click listeners to a certain view ids in the row
     */
    fun setViewClickModels(viewClickModels: List<ViewClickModel<Binding, DM>>?): BaseViewHolder<Binding, DM>? {
        this.viewClickModels = viewClickModels
        setViewCLickListeners()
        return this
    }

    /**
     * Set listener of row click
     */
    fun setRowClickListener(cLickListener: RowCLickListener<Binding, DM>?) {
        if (cLickListener != null) itemView.setOnClickListener {
            cLickListener!!.onRowClicked(
                binding!!, adapterPosition, dataModel
            )
        }
    }

    /**
     * Set listener of view ids clicks
     */
    private fun setViewCLickListeners() {

        if (viewClickModels != null && viewClickModels!!.isNotEmpty())
            for (model in viewClickModels!!)
                if (itemView.findViewById<View?>(model.viewId) != null)
                    binding!!.root.findViewById<View>(model.viewId)
                        .setOnClickListener {
                            model.cLickListener
                            model.cLickListener.onRowClicked(
                                binding,
                                adapterPosition,
                                dataModel
                            )
                        }
    }

    open fun onBind(position: Int, dm: DM) {
        dataModel = dm
        bindView(position)
    }

    /**
     * Bind the row view and its data to it
     */
    open fun onBind(position: Int, dm: DM, itemCount: Int) {
        this.itemCount = itemCount
        dataModel = dm
        bindView(position)
    }

    private fun bindView(position: Int) {
        try {
            binding!!.setVariable(BR.position, position)
            binding!!.setVariable(BR.itemCount, itemCount)
            binding!!.setVariable(bindingVariable, dataModel)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    interface RowCLickListener<Binding, DM> {
        fun onRowClicked(binding: Binding?, position: Int, dataModel: DM?)
    }

}
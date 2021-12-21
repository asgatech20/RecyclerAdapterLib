package com.asga.recycler_adapter.adapters

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.BR
import java.util.*

open class BaseViewHolder<Binding : ViewDataBinding, DM:Any> : RecyclerView.ViewHolder {
    var binding: Binding? = null
    open var cLickListener: RowCLickListener<Binding, DM>? = null
    open var viewClickModels: List<ViewClickModel<Binding, DM>>? = null
    open var bindingVariable = -1
    open var dataModel: DM? = null
    private var itemCount = 0

    constructor(binding: Binding) : this(binding, -1)

    constructor(binding: Binding, bindingVariable: Int) : this(binding, null, bindingVariable, null)


    constructor(
        binding: Binding,
        cLickListener: RowCLickListener<Binding, DM>?,
        bindingVariable: Int,
        viewClickModels: List<ViewClickModel<Binding, DM>>?
    ) : super(binding.root) {
        this.binding = binding
        this.cLickListener = cLickListener
        this.bindingVariable = bindingVariable
        setViewClickModels(viewClickModels)
    }

    open fun addViewClickModel(viewClickModel: ViewClickModel<Binding, DM>?): BaseViewHolder<Binding, DM>? {
        if (viewClickModels == null) viewClickModels = ArrayList()
        viewClickModels!!.plus(viewClickModel)
        setListeners()
        return this
    }

    open fun setViewClickModels(viewClickModels: List<ViewClickModel<Binding, DM>>?): BaseViewHolder<Binding, DM>? {
        this.viewClickModels = viewClickModels
        setListeners()
        return this
    }

    private fun setListeners() {
        if (cLickListener != null) itemView.setOnClickListener {
            cLickListener!!.onRowClicked(
                binding!!, adapterPosition, dataModel
            )
        }
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

    open fun onBind(position: Int, dm: DM, itemCount: Int) {
        this.itemCount = itemCount
        dataModel = dm
        bindView(position)
    }

    private  fun bindView(position: Int) {
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
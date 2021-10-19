package com.asga.recycler_adapter

import androidx.annotation.IdRes

data class ViewClickModel<Binding, DM>(
    @IdRes var viewId: Int,
    var cLickListener: BaseViewHolder.RowCLickListener<Binding, DM>
)
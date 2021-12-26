package com.asga.recycler_adapter.adapters

import androidx.annotation.IdRes

/**
 * data object class that used to set click listeners for certain view IDs
 */
data class ViewClickModel<Binding, DM>(
    @IdRes var viewId: Int,
    var cLickListener: BaseViewHolder.RowCLickListener<Binding, DM>
)
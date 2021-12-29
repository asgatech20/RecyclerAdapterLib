package com.asga.recycler_adapter.data

import androidx.annotation.IdRes
import com.asga.recycler_adapter.view_holders.BaseViewHolder

/**
 * data object class that used to set click listeners for certain view IDs
 */
data class ViewClickModel<Binding, DM>(
    @IdRes var viewId: Int,
    var cLickListener: BaseViewHolder.RowCLickListener<Binding, DM>
)
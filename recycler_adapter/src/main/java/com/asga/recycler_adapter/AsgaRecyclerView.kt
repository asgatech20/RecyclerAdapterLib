package com.asga.recycler_adapter

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author: Muhammad Noamany
 * @Date: 12/21/2021
 * @Email: muhammadnoamany@gmail.com
 */
class AsgaRecyclerView : RelativeLayout {
    private lateinit var recyclerView: RecyclerView

    constructor(context: Context?) :
            super(context, null)


    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs, 0) {
        val typedAttributeSet = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AsgaRecyclerView,
            0,
            0
        )
        try {
            readXmlAttr(typedAttributeSet)
        } finally {
            typedAttributeSet.recycle()
        }
        init()
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr, 0)

    /**
     * Fetch attr from xml view or init with the default values
     */
    private fun readXmlAttr(typedAttributeSet: TypedArray) {

    }

    /**
     * Init the whole view
     */
    private fun init() {
        // configure functions of loading view and recycler view
        initRecyclerView()
    }

    /**
     * Add the Recycler view to the parent view
     */
    private fun initRecyclerView() {
        recyclerView = RecyclerView(context)
        addView(recyclerView)
    }
}
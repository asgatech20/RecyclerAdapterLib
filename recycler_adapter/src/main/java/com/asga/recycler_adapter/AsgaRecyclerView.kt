package com.asga.recycler_adapter

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.adapters.BaseAdapter

/**
 * @Author: Muhammad Noamany
 * @Date: 12/21/2021
 * @Email: muhammadnoamany@gmail.com
 */
class AsgaRecyclerView : RelativeLayout {
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: Int = 0
    private var gridCount: Int = 0

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
     * @layoutManager the layout manager type for the recycler
     */
    private fun readXmlAttr(typedAttributeSet: TypedArray) {
        layoutManager =
            typedAttributeSet.getInt(R.styleable.AsgaRecyclerView_layoutManagerType, 0)
        gridCount =
            typedAttributeSet.getInt(R.styleable.AsgaRecyclerView_gridCount, 2)
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
        recyclerView.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )
        addView(recyclerView)
    }

    /**
     * set the adapter for the recycler view
     */
    fun setAdapter(adapter: BaseAdapter<*, *>) {
        setLayoutManager()
        recyclerView.adapter = adapter;
    }

    /**
     * Set the layout manager type according to the xml value type
     */
    private fun setLayoutManager() {
        when (layoutManager) {
            0 -> recyclerView.layoutManager = // case vertical layout manager
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            1 -> recyclerView.layoutManager =  // case horizontal layout manager
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            2 -> recyclerView.layoutManager =  // case vertical grid manager
                GridLayoutManager(context, gridCount, RecyclerView.VERTICAL, false)
            3 -> recyclerView.layoutManager =  // case horizontal grid manager
                GridLayoutManager(context, gridCount, RecyclerView.HORIZONTAL, false)
        }
    }
}
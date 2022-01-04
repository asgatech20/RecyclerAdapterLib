package com.asga.recycler_adapter


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asga.recycler_adapter.adapters.BaseAdapter
import com.facebook.shimmer.ShimmerFrameLayout

class AsgaRecyclerView : RelativeLayout {
    private val NORMAL_LOADING_VIEW_TYPE = 1
    private val CUSTOM_LOADING_VIEW_TYPE = 2
    private val SHIMMER_LOADING_VIEW_TYPE = 3
    private val DEAFULT_KEYS_VALUE = -1
    private val VERTICAL_LAOUT_MANAGER = 0
    private val HORIZONTAL_LAOUT_MANAGER = 1
    private val VERTICAL_GRID_LAOUT_MANAGER = 2
    private val HORIZONTAL_GRID_LAOUT_MANAGER = 3
    private lateinit var emptyViewContainer: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingContainer: RelativeLayout
    private lateinit var shimmerContainer: ShimmerFrameLayout
    private var emptyViewRes: Int = 1
    private var loadingView: Int = 1
    private var customLoadingViewRes: Int = -1
    private var shimmerLoadingViewRes: Int = -1
    private var shimmerRowCount: Int = 1
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
     */
    private fun readXmlAttr(typedAttributeSet: TypedArray) {
        loadingView =
            typedAttributeSet.getInt(
                R.styleable.AsgaRecyclerView_loadingView,
                NORMAL_LOADING_VIEW_TYPE
            )
        emptyViewRes =
            typedAttributeSet.getResourceId(
                R.styleable.AsgaRecyclerView_emptyViewRes,
                DEAFULT_KEYS_VALUE
            )
        shimmerLoadingViewRes =
            typedAttributeSet.getResourceId(
                R.styleable.AsgaRecyclerView_shimmerLoadingViewRes,
                R.color.greyColor
            )
        customLoadingViewRes =
            typedAttributeSet.getResourceId(
                R.styleable.AsgaRecyclerView_customLoadingViewRes,
                R.color.greyColor
            )
        shimmerRowCount =
            typedAttributeSet.getInt(
                R.styleable.AsgaRecyclerView_shimmerRowCount,
                DEAFULT_KEYS_VALUE
            )

        layoutManager =
            typedAttributeSet.getInt(
                R.styleable.AsgaRecyclerView_layoutManagerType,
                VERTICAL_LAOUT_MANAGER
            )
        gridCount =
            typedAttributeSet.getInt(R.styleable.AsgaRecyclerView_gridCount, DEAFULT_KEYS_VALUE)
    }

    /**
     * Init the whole view
     */
    private fun init() {
        // configure functions of loading view and recycler view
        initLoadingView()
        initRecyclerView()
        initEmptyView()
    }

    /**
     * Add the empty status view to the parent layout
     */
    private fun initEmptyView() {
        if (emptyViewRes == -1) return
        emptyViewContainer = RelativeLayout(context)
        emptyViewContainer.layoutParams = LayoutParams( // set container params
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        emptyViewContainer.visibility = View.GONE
        emptyViewContainer.gravity = Gravity.CENTER
        emptyViewContainer.addView(inflateEmptyView()) // add the view to the parent
        addView(emptyViewContainer) // add the view to the component

    }

    /**
     * Configure and inflate the empty view layout resource
     */
    private fun inflateEmptyView(): View? {
        var inflater = LayoutInflater.from(context)
        var resource = inflater.inflate( // inflate the resource layout
            emptyViewRes,
            this,
            false
        )
        return resource
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
     * Add the loading view to the parent view
     */
    private fun initLoadingView() {
        initLoadingContainer()
        addView(loadingContainer)
    }

    /**
     * Configuring of loading view
     */
    private fun initLoadingContainer() {
        loadingContainer = RelativeLayout(context)
        loadingContainer.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        loadingContainer.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
        loadingContainer.setVerticalGravity(Gravity.CENTER_VERTICAL)
        loadingContainer.isClickable = true
        loadingContainer.setBackgroundColor(Color.parseColor("#00000000"))
        when (loadingView) {
            //case 1 : show normal progress if no custom passed
            NORMAL_LOADING_VIEW_TYPE -> {
                addProgressLoadingViewToLoadingContainer()
            }
            //case 2 : show custom loading
            CUSTOM_LOADING_VIEW_TYPE -> {
                addCustomLoadingViewToLoadingContainer()
            }
            //case 3 : show shimmer loading
            SHIMMER_LOADING_VIEW_TYPE -> {
                addShimmerLoadingViewToLoadingContainer()
            }
        }
    }

    /**
     * Inflate the loading view as progress bar loading
     */
    private fun addProgressLoadingViewToLoadingContainer() {
        var defaultLoadingView = ProgressBar(context)
        loadingContainer.addView(defaultLoadingView)
    }

    /**
     * Inflate the loading view from custom layout passed by the users
     */
    private fun addCustomLoadingViewToLoadingContainer() {
        val customLoadingView = inflate(
            context,
            customLoadingViewRes,
            null
        )
        loadingContainer.addView(customLoadingView)
    }

    /**
     * Inflate the loading view from custom layout passed by the users as Shimmer animation loading
     */
    private fun addShimmerLoadingViewToLoadingContainer() {
        //build Shimmer Container
        createShimmerFrameLayout()
        //Adding the shimmerContainer to the some view
        loadingContainer.addView(shimmerContainer)
    }

    /**
     * Create shimmer animation frame container
     */
    private fun createShimmerFrameLayout() {
        shimmerContainer = ShimmerFrameLayout(context)
        shimmerContainer.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        var linear = LinearLayout(context)
        linear.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        linear.orientation = LinearLayout.VERTICAL
        for (i in 0..shimmerRowCount) {
            LayoutInflater.from(context).inflate(shimmerLoadingViewRes, linear, true)
        }
        shimmerContainer.addView(linear)
        shimmerContainer.showShimmer(true)
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
            VERTICAL_LAOUT_MANAGER -> recyclerView.layoutManager = // case vertical layout manager
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            HORIZONTAL_LAOUT_MANAGER -> recyclerView.layoutManager =
                    // case horizontal layout manager
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            VERTICAL_GRID_LAOUT_MANAGER -> recyclerView.layoutManager =
                    // case vertical grid manager
                GridLayoutManager(context, gridCount, RecyclerView.VERTICAL, false)
            HORIZONTAL_GRID_LAOUT_MANAGER -> recyclerView.layoutManager =
                    // case horizontal grid manager
                GridLayoutManager(context, gridCount, RecyclerView.HORIZONTAL, false)
        }
    }

    /**
     * Update Loading Status
     */
    fun setLoadingStatus(Loading: Boolean) {
        if (Loading)
            loadingContainer.visibility = View.GONE
        else
            loadingContainer.visibility = View.VISIBLE
    }

    /**
     * Show the empty row resource
     */
    fun setShowEmpty(showEmpty: Boolean) {
        if (getEmptyViewRes() == -1) return
        if (showEmpty)
            getEmptyViewContainer().visibility = View.VISIBLE
        else
            getEmptyViewContainer().visibility = View.GONE
    }

    /********************************************************* Getters ********************************************/
    fun getEmptyViewRes(): Int {
        return emptyViewRes
    }

    fun getEmptyViewContainer(): RelativeLayout {
        return emptyViewContainer
    }

    fun getLoadingContainer(): RelativeLayout {
        return loadingContainer
    }

    /********************************************************* End Of Getters ***************************************/
}
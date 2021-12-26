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
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout


/**
 * @Author: Muhammad Noamany
 * @Date: 12/21/2021
 * @Email: muhammadnoamany@gmail.com
 */
class AsgaRecyclerView : RelativeLayout {
    private lateinit var shimmerBuilder: Shimmer.ColorHighlightBuilder
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingContainer: RelativeLayout
    private lateinit var shimmerContainer:ShimmerFrameLayout
    private var loadingView: Int = 1

    private var customLoadingViewRes: Int = -1

    private var shimmerLoadingViewRes: Int = -1
    private var shimmerRowCount: Int = 1


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
            typedAttributeSet.getInt(R.styleable.AsgaRecyclerView_loadingView, 1)
        shimmerLoadingViewRes =
            typedAttributeSet.getResourceId(R.styleable.AsgaRecyclerView_shimmerLoadingViewRes, R.color.greyColor)
        customLoadingViewRes =
            typedAttributeSet.getResourceId(R.styleable.AsgaRecyclerView_customLoadingViewRes, R.color.greyColor)
        shimmerRowCount =
            typedAttributeSet.getInt(R.styleable.AsgaRecyclerView_shimmerRowCount, -1)
    }

    /**
     * Init the whole view
     */
    private fun init() {
        // configure functions of loading view and recycler view
        initLoadingView()
        initRecyclerView()
    }
    /**
     * Add the Recycler view to the parent view
     */
    private fun initRecyclerView() {
        recyclerView = RecyclerView(context)
        addView(recyclerView)
    }
    /**
     * Add the loading view to the parent view
     */
    private fun initLoadingView() {
        initLoadingContainer()
        addView(loadingContainer)
    }

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
            1 -> {
                addProgressLoadingViewToLoadingContainer()
            }
            //case 2 : show custom loading
            2 -> {
                addCustomLoadingViewToLoadingContainer()
            }
            //case 3 : show shimmer loading
            3 -> {
                addShimmerLoadingViewToLoadingContainer()
            }
        }
    }

    private fun addProgressLoadingViewToLoadingContainer() {
        var defaultLoadingView = ProgressBar(context)
        loadingContainer.addView(defaultLoadingView)
    }

    private fun addShimmerLoadingViewToLoadingContainer() {
        //build Shimmer Container
        buildShimmerContainer()
        //Adding the shimmerContainer to the some view
        loadingContainer.addView(shimmerContainer)
    }

    private fun addCustomLoadingViewToLoadingContainer() {
        val customLoadingView = inflate(
            context,
            customLoadingViewRes,
            null
        )
        loadingContainer.addView(customLoadingView)
    }

    private fun buildShimmerContainer() {
        var shimmer = createShimmer()
        val shimmerLoadingView = inflate(
            context,
            shimmerLoadingViewRes, null
        )
        //Creating the shimmer frame layout
        createShimmerFrameLayout(shimmerLoadingView, shimmer)
    }

    private fun createShimmerFrameLayout(
        shimmerLoadingView: View,
        shimmer: Shimmer?
    ) {
        shimmerContainer = ShimmerFrameLayout(context)
        shimmerContainer.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        var linear = LinearLayout(context)
        linear.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
       // linear.setBackgroundColor(Color.parseColor("#B00020"))

        linear.orientation = LinearLayout.VERTICAL
        for (i in 0..shimmerRowCount) {
            LayoutInflater.from(context).inflate(shimmerLoadingViewRes, linear, true)
        }
        shimmerContainer.addView(linear)
        shimmerContainer.showShimmer(true)
    }
    private fun createShimmer(): Shimmer? {
        //Create shimmer builder
         shimmerBuilder = Shimmer.ColorHighlightBuilder()
            .setDuration(1200)
            .setIntensity(0.9f)
            .setDropoff(0.9f)
            .setBaseAlpha(0.6f)
            .setHighlightAlpha(1f)
        //Create shimmer
        var shimmer = shimmerBuilder.build()
        return shimmer
    }

    fun updateShimmerColor(hexShimmerBaseColor: Int, hexShimmerHighLightColor: Int){
        shimmerBuilder.setBaseColor(hexShimmerBaseColor)?.setHighlightColor(hexShimmerHighLightColor)
    }
    @BindingAdapter("stopShimmerLoading")
    fun stopLoading( view:View , Loading: Boolean) {
        if (Loading)
            loadingContainer.visibility = View.GONE
        else
            loadingContainer.visibility = View.VISIBLE
    }
}
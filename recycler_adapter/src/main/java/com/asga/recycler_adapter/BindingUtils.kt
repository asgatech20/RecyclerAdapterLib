package com.asga.recycler_adapter

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableInt
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter(value = ["updateAdapter"])
fun updateAdapter(recyclerView: RecyclerView, data: MutableLiveData<List<Any>>?) {
    if (data == null) return
    if (recyclerView.tag != null && recyclerView.tag is MutableLiveData<*>) //case data subscribed before
        return
    //observing the liveData to keep the adapter updated
    data.observe(
        (recyclerView.context as LifecycleOwner),
        Observer { objects: List<Any> ->
            if (recyclerView.adapter != null && recyclerView.adapter is BaseAdapter<*, *>) {
                if ((recyclerView.adapter as BaseAdapter<*, *>?)!!.isEmpty()) {
                    recyclerView.scheduleLayoutAnimation()
                }
                (recyclerView.adapter as BaseAdapter<*, Any>?)!!.updateDataList(objects)
            }
        })
    recyclerView.tag = data
}


@BindingAdapter("setRecyclerVerticalDivider")
fun setRecyclerVerticalDivider(recyclerView: RecyclerView, set: Boolean) {
    if (!set) return
    val itemDecorator = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
    itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.context, R.drawable.divider)!!)
    recyclerView.addItemDecoration(itemDecorator)
}

@BindingAdapter("setRecyclerHorizontalDivider")
fun setRecyclerHorizontalDivider(recyclerView: RecyclerView, set: Boolean) {
    if (!set) return
    val itemDecorator =
        DividerItemDecoration(recyclerView.context, DividerItemDecoration.HORIZONTAL)
    itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.context, R.drawable.divider)!!)
    recyclerView.addItemDecoration(itemDecorator)
}

@BindingAdapter(value = ["setHorizontalLayoutManager"], requireAll = true)
fun setHorizontalLayoutManager(view: RecyclerView, set: Boolean) {
    if (set) view.layoutManager =
        LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
}

@BindingAdapter(value = ["setVerticalLayoutManager"], requireAll = true)
fun setVerticalLayoutManager(recyclerView: RecyclerView, set: Boolean) {
    if (set) recyclerView.layoutManager =
        LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
}

@BindingAdapter(value = ["setVerticalGridLayoutManager"], requireAll = true)
fun setVerticalGridLayoutManager(recyclerView: RecyclerView, spanCount: Int) {
    if (spanCount > 0) recyclerView.layoutManager =
        GridLayoutManager(recyclerView.context, spanCount, RecyclerView.VERTICAL, false)
}

@BindingAdapter(value = ["setHorizontalGridLayoutManager"], requireAll = true)
fun setHorizontalGridLayoutManager(view: RecyclerView, spanCount: Int) {
    if (spanCount > 0) view.layoutManager =
        GridLayoutManager(view.context, spanCount, RecyclerView.HORIZONTAL, false)
}

@BindingAdapter(value = ["totalItemCount"])
fun setTotalItemCount(recyclerView: RecyclerView, totalItemCount: ObservableInt?) {
    if (totalItemCount == null) return
    totalItemCount.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            if (recyclerView.adapter != null && recyclerView.adapter is BaseLoaderAdapter<*,*>)
                    (recyclerView.adapter as BaseLoaderAdapter<*,*>?)!!.setTotalItemCount(
                totalItemCount.get()
            )
        }
    })
}

@BindingAdapter("radius_percent")
fun radius_percent(view: View, percent: Int) {
    val approxWidth: Float = getScreenWidth(view.context) * (percent / 100f)
    val params = view.layoutParams
    params.width = Math.round(approxWidth)
    params.height = Math.round(approxWidth)
    view.layoutParams = params
}

fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    if(Build.VERSION.SDK_INT < 30){

        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(dm)
    }else{
        context.display?.getRealMetrics(dm)

    }
    return dm.widthPixels
}
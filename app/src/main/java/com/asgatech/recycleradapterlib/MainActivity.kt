package com.asgatech.recycleradapterlib

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.asga.recycler_adapter.adapters.BasePaginationAdapter
import com.asgatech.recycleradapterlib.data.ItemModel
import com.asgatech.recycleradapterlib.databinding.ActivityMainBinding
import com.asgatech.recycleradapterlib.databinding.LayoutItemRowBinding

class MainActivity : AppCompatActivity() {
    private var adapter: BasePaginationAdapter<LayoutItemRowBinding, ItemModel>? = null
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setAdapter()
    }

    private fun setAdapter() {
        adapter = BasePaginationAdapter(R.layout.layout_item_row)
        binding!!.recycler.setAdapter(adapter!!)
        adapter!!.setPaginationHandler(object : BasePaginationAdapter.PaginationHandler<ItemModel> {
            override fun onLoadMore(page: Int, totalRows: Int) {
                val handler = Handler()
                handler.postDelayed({
                    initMockData()
                }, 5000)
            }
        })
        //init the adapter and set assign it as the recycler adapter
        val handler = Handler()
        handler.postDelayed({
            binding!!.recycler.setLoadingStatus(true)
            initMockData()
        }, 5000)

    }


    private fun initMockData() {
        // set some fake data to be used to test the adapter sample
        var itemsList = mutableListOf<ItemModel>()
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))
        itemsList.add(ItemModel("John Star", "johnstar@gmail.com"))
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))
        itemsList.add(ItemModel("John Star", "johnstar@gmail.com"))
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))
        itemsList.add(ItemModel("John Star", "johnstar@gmail.com"))
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))
        itemsList.add(ItemModel("John Star", "johnstar@gmail.com"))
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))
        itemsList.add(ItemModel("John Star", "johnstar@gmail.com"))
        itemsList.add(ItemModel("John Doe", "johndoe@gmail.com"))

        // pass the data to the adapter
        adapter!!.updateDataList(itemsList)
        binding!!.recycler.setShowEmpty(itemsList.isEmpty())
    }

}
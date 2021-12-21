package com.asgatech.recycleradapterlib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.asga.recycler_adapter.adapters.BaseAdapter
import com.asgatech.recycleradapterlib.data.ItemModel
import com.asgatech.recycleradapterlib.databinding.ActivityMainBinding
import com.asgatech.recycleradapterlib.databinding.LayoutItemRowBinding

class MainActivity : AppCompatActivity() {
    private var adapter: BaseAdapter<LayoutItemRowBinding, ItemModel>? = null
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setAdapter()
        initMockData()
    }

    private fun setAdapter() {
        // init the adapter and set assign it as the recycler adapter
        adapter = BaseAdapter(R.layout.layout_item_row)
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
        adapter!!.setDataList(itemsList)
    }

}
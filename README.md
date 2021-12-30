# Asga Recycler Adapter Lib.

An Android library that make it easy to handle lists of data using custom android component and custom adapters for both single and paginated data.

### Quick Setup

### 1- Include the library
Add the Jitpack maven repository if you don't have it yet:

``` gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Reference the library from your module's build.gradle:

``` gradle
dependencies {
    [...]
    implementation 'com.github.asgatech20:NetworkLib:1.0'
}
```

Add source and target compatibility to your module's build.gradle:

``` gradle
android {
    ...
    compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
}
```
### 2- Using of the component
Add the compnent to your xml as below:
``` xml
 <com.asga.recycler_adapter.AsgaRecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
```

### 3- Configuring of the loading view
To be able to show a loading view animation during loading of the data, you can choose between three sets of loading types through your xml as below:
- **Progress Loading view**: 
set "app:loadingView" key in xml as below
 ``` xml
 <com.asga.recycler_adapter.AsgaRecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="@dimen/huge"
            app:loadingView="progressLoading" />
```
- **Custom Loading view**: 
set "app:loadingView" key to [customLoading] and set your custom loading layout resource in "app:customLoadingViewRes" key in xml as below
 ``` xml
       <com.asga.recycler_adapter.AsgaRecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="@dimen/huge"
            app:loadingView="customLoading" 
            app:customLoadingViewRes="@layout/layout_cutom_loading"/>
```

- **Shimmer Loading view animation**: 
set "app:loadingView" key to [shimmerLoading] and set your custom loading layout resource in "app:shimmerLoadingViewRes" key in xml as below:
 ``` xml
<com.asga.recycler_adapter.AsgaRecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="@dimen/huge"
            app:loadingView="shimmerLoading"
            app:shimmerLoadingViewRes="@layout/layout_cutom_loading"/>
```
###### And using the binding adapter function of [app:setLoadingStatus="@{laodingKey}"] through xml, you can decide when to show or hide the loading.

### 4- Configuring of the Empty view layout
To be able to show a view that being show in case the list has no data to show, you can set the key of "app:emptyViewRes" to have your empty view layout resource and using the binding adapter function of [app:showEmpty="@{ifDataEmptyKey}"] through xml to decide when to show or hide the empty view resource as below:
 ``` xml
<com.asga.recycler_adapter.AsgaRecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="@dimen/huge"
            app:loadingView="shimmerLoading"
            app:emptyViewRes="@layout/layout_cutom_loading"
            app:showEmpty="@{ifDataEmptyKey}"/>
```
### 5- Configuring of the Recycler and its adapter
**you can specify the recycler view options from xml as below:**
``` xml 
 <com.asga.recycler_adapter.AsgaRecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:gridCount="3"
            app:layoutManagerType="vertical" />
```
- **Vertical items recycler view:**
set "app:layoutManagerType" key to [vertical] in xml.
- **Horizontal items recycler view:**
set "app:layoutManagerType" key to [horizontal] in xml.
- **Vertical Grid items recycler view:**
set "app:layoutManagerType" key to [verticalGrid] and "app:gridCount" key to your column number you want in the grid in xml.

- **Horizontal Grid items recycler view:**
set "app:layoutManagerType" key to [horizontal] and "app:gridCount" key to your column number you want in the grid in xml.

### 6- Configuring of the Recycler adapter
**There are two types of the recycler adapters in this component one for normal adapter and another for helping in paginated data, you can use them as below:** 
-**Normal Adapter:**
you can use this adapter if you welling to use the component to show single list data (Not paginated) as bellow:
- Create your xml row layout file as bindable layout and add your data class object as "dataModel" key. (you can check the example for more details).
- In your activity or fragment, create an Instance from BaseAdapter class as bellow:
``` java
    private var adapter: BaseAdapter<LayoutItemRowBinding, ItemModel>? = null
...
adapter = BaseAdapter(R.layout.layout_item_row)
asgaRecyclerView.setAdapter(adapter!!)
adapter!!.setData(yourList)
```
-**Paginated Adapter:**
you can use this adapter if you welling to use the component to show paginated data as bellow:
- Create your xml row layout file as bindable layout and add your data class object as "dataModel" key. (you can check the example for more details).
- In your activity or fragment, create an Instance from BaseAdapter class as bellow:
``` java
    private var adapter: BasePaginationAdapter<LayoutItemRowBinding, ItemModel>? = null
...
adapter = BasePaginationAdapter(R.layout.layout_item_row)
asgaRecyclerView.setAdapter(adapter!!)
adapter!!.setData(yourList)
```
If you want to update your dataList and add another data to it just call:
``` jav 
        adapter!!.updateDataList(itemsList)
```
To make use of the pagination feature, you can use the pagination listener to have the ability to call your next data nad append it to the adapter like bellow:
``` java 
 adapter!!.setPaginationHandler(object : BasePaginationAdapter.PaginationHandler<ItemModel> {
            override fun onLoadMore(page: Int, totalRows: Int) {
               // call your next page data
            }
        })
```

 Note : Set the pagination handler after you set the adapter to the recycler (this is a must for the pagination to work correctly), check as bellow: 

``` java 
 bindingasgaRecyclerView.setAdapter(adapter!!)
        adapter!!.setPaginationHandler(object : BasePaginationAdapter.PaginationHandler<ItemModel> {
            override fun onLoadMore(page: Int, totalRows: Int) {
               // call your next page data
            }
        })
```
To be able to set a click listener for the entire row, you can do it as bellow: 
``` java 
adapter!!.setRowClickListener(object : BaseViewHolder.RowCLickListener<LayoutItemRowBinding, ItemModel> {
            override fun onRowClicked(binding: LayoutItemRowBinding?, position: Int, dataModel: ItemModel?) {
// DO what ever you want if user clicks the row
            }

        })
```
To be able to set a click listener for a certain view id in the row layout, you can do it as bellow: 
``` java 
 var idClickModel = ViewClickModel<LayoutItemRowBinding, ItemModel>(R.id.cetainViewId, object: BaseViewHolder.RowCLickListener<LayoutItemRowBinding, ItemModel> {
            override fun onRowClicked(binding: LayoutItemRowBinding?, position: Int, dataModel: ItemModel?) {
                // Your code that will be excuted when user click on this view id
            }
        })
        var idClickModelTwo = ViewClickModel<LayoutItemRowBinding, ItemModel>(R.id.cetainViewId, object: BaseViewHolder.RowCLickListener<LayoutItemRowBinding, ItemModel> {
            override fun onRowClicked(binding: LayoutItemRowBinding?, position: Int, dataModel: ItemModel?) {
                // Your code that will be excuted when user click on this view id
            }
        })
        adapter!!.setViewClickModels(listOf(idClickModel,idClickModelTwo) )
```
### Happy Coding

## Authors

* [Muhammad Noamany](https://github.com/muhammadnomany25)
* [Ibrahim Ali](https://github.com/IbrahimAli2017)
* [Ahmed Saber](https://github.com/Ahmed-Saber-25)

## Owner

* [AsgaTech Company](https://github.com/asgatech20)


### License

    Copyright 2021 AsgaTech Company.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


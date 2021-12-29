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


### Happy Coding

## Authors

* [Muhammad Noamany](https://github.com/muhammadnomany25)
* [Ibrahim Ali](https://github.com/IbrahimAli2017)

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


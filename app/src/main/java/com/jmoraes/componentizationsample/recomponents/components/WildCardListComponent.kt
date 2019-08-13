/*
 * Copyright (C) 2019 Netflix, Inc. All rights reserved.
 *
 * Created by Juliano Moraes
 */

package com.jmoraes.componentizationsample.recomponents.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.recomponents.models.Wildcard
import com.netflix.recomponents.ListComponentAdapter
import com.netflix.recomponents.UIComponent
import com.netflix.recomponents.UIComponentForList

data class WildcardListComponentState(
    val isLoading: Boolean = false,
    val wildcardList: List<Wildcard>?
)

class WildcardListComponent(
    container: ViewGroup
) : UIComponent<WildcardListComponentState>() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildcardDataSetList: MutableList<WildcardComponentState> = mutableListOf()

    private val rootView = LayoutInflater.from(container.context).inflate(
        R.layout.wildcard_list,
        container,
        true
    ) as ViewGroup

    private val recyclerView by lazy {
        rootView.findViewById<RecyclerView>(R.id.wildcardList)
    }

    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        rootView.findViewById<SwipeRefreshLayout>(R.id.generic_list_srl)
    }

    private val adapter = object :
        ListComponentAdapter<WildcardComponentState>() {
        override fun getComponentForList(viewType: Int): UIComponentForList<WildcardComponentState> {
            return WildcardComponent(
                LayoutInflater.from(container.context)
                    .inflate(R.layout.wildcard_list_item, container, false) as ViewGroup
            )
        }
    }

    init {
        recyclerView.layoutManager = LinearLayoutManager(container.context)
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }

    override fun render(state: WildcardListComponentState) {
        if (state.wildcardList.isNullOrEmpty()) {
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
        }

        swipeRefreshLayout.isRefreshing = state.isLoading

        wildcardDataSetList.clear()

        state.wildcardList?.map {
            wildcardDataSetList.add(
                WildcardComponentState(
                    wildcard = it
                )
            )
        }

        adapter.update(wildcardDataSetList)
    }
}

fun getRootView(rootView: ViewGroup, layoutRes: Int, forceInflation: Boolean = false): ViewGroup {
    // This checks if the rootView is already inflated
    return if (rootView.childCount > 0 && !forceInflation) {
        rootView
    } else {
        LayoutInflater.from(rootView.context).inflate(
            layoutRes,
            rootView,
            true
        ) as ViewGroup
    }
}

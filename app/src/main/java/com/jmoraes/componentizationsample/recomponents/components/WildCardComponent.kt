/*
 * Copyright (C) 2019 Netflix, Inc. All rights reserved.
 *
 * Created by Juliano Moraes
 */

package com.jmoraes.componentizationsample.recomponents.components

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.recomponents.models.Wildcard
import com.netflix.recomponents.ComparableById
import com.netflix.recomponents.UIComponentForList

data class WildcardComponentState(
    val wildcard: Wildcard
): ComparableById {

    override fun getIdForComparison(): String {
        return wildcard.id
    }
}

class WildcardComponent(private val container: ViewGroup) : UIComponentForList<WildcardComponentState>(container) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildcardMessage by lazy {
        container.findViewById<TextView>(R.id.title)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wildcardCreated by lazy {
        container.findViewById<TextView>(R.id.by)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var wildcardDataModel: WildcardComponentState? = null

    override fun render(state: WildcardComponentState) {
        wildcardDataModel = state
        wildcardMessage.text = state.wildcard.message
        wildcardCreated.text = container.context.getString(R.string.wildcard_created_at, state.wildcard.createdBy)
    }
}
/*
 * Copyright (C) 2019 Netflix, Inc. All rights reserved.
 *
 * Created by Juliano Moraes
 */

package com.jmoraes.componentizationsample.recomponents.redux.reducers

import com.jmoraes.componentizationsample.recomponents.redux.actions.DataFetched
import com.jmoraes.componentizationsample.recomponents.redux.actions.FetchData
import com.jmoraes.componentizationsample.recomponents.redux.state.AppState
import com.netflix.recomponents.Action

fun appReducer(action: Action, state: AppState? = AppState()): AppState {
    // if no state has been provided, create the default state
    var newState = state ?: AppState()
    when (action) {
        is FetchData -> {
            newState = newState.copy(isFetching = true)
        }
        is DataFetched -> {
            newState = newState.copy(isFetching = false, wildcards = action.wildcards)
        }
    }

    return newState
}

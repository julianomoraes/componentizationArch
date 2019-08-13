/*
 * Copyright (C) 2019 Netflix, Inc. All rights reserved.
 *
 * Created by Juliano Moraes
 */

package com.jmoraes.componentizationsample.recomponents.redux.middleware

import com.jmoraes.componentizationsample.recomponents.models.getWildcardMockList
import com.jmoraes.componentizationsample.recomponents.redux.actions.DataFetched
import com.jmoraes.componentizationsample.recomponents.redux.actions.FetchData
import com.jmoraes.componentizationsample.recomponents.redux.state.AppState
import com.netflix.recomponents.Action
import com.netflix.recomponents.DispatchFunction
import com.netflix.recomponents.Next
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun appMiddleware(
    dispatch: DispatchFunction,
    state: AppState,
    action: Action,
    next: Next<AppState>
): Action {
    if (action is FetchData) {
        Observable.just(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                dispatch(
                    DataFetched(
                        getWildcardMockList(action.projectId)
                    )
                )
            }
            .subscribe()
    }
    return next(state, action, dispatch)
}

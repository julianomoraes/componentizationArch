package com.jmoraes.componentizationsample.recomponents.redux.actions

import com.jmoraes.componentizationsample.recomponents.models.Wildcard
import com.netflix.recomponents.Action

data class FetchData(val projectId: Int?) : Action
data class DataFetched(val wildcards: List<Wildcard>) : Action
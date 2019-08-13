package com.jmoraes.componentizationsample.recomponents.redux.state

import com.jmoraes.componentizationsample.recomponents.models.Project
import com.jmoraes.componentizationsample.recomponents.models.Wildcard
import com.netflix.recomponents.StateType

data class AppState(
    var isFetching: Boolean = false,
    val wildcards: List<Wildcard>? = listOf(),
    val projects: List<Project> = listOf(
        Project(
            id = "1",
            title = "Message Thread 1"
        ),
        Project(
            id = "2",
            title = "Message Thread 2"
        )
    ),
    val selectedProject: Project? = null
) : StateType
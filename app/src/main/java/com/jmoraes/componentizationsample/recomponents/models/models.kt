package com.jmoraes.componentizationsample.recomponents.models

data class Wildcard(
    val createdAt: String? = null,
    val createdBy: String? = null,
    val id: String = "__id__",
    val message: String? = null,
    val startTime: String? = null
)

fun getWildcardMockList(projectId: Int?): List<Wildcard> {
    val list = mutableListOf<Wildcard>()
    for (i in 0..1000) {
        list.add(
            Wildcard(
                id = i.toString(),
                message = "#Thread $projectId Message $i",
                createdBy = if (projectId == 1) "Pedro" else "Joao"
            )
        )
    }
    return list
}

data class Project (
    val id: String = "__id__",
    val title: String = ""
)
package com.netflix.componentizationV1

import io.reactivex.Observable

interface UIComponent<T> {
    fun getContainerId(): Int
    fun getUserInteractionEvents(): Observable<T>
}